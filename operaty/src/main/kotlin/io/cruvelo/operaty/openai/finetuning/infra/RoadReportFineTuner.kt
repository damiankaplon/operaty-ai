package io.cruvelo.operaty.openai.finetuning.infra

import io.cruvelo.operaty.openai.files.OpenAiFileUploader
import io.cruvelo.operaty.openai.finetuning.FineTunedModel
import io.cruvelo.operaty.openai.finetuning.FineTunedModelRepository
import io.cruvelo.operaty.openai.finetuning.FineTunerSourceProvider
import io.cruvelo.operaty.openai.finetuning.JsonlFineTuneEncoder
import io.cruvelo.operaty.openai.finetuning.JsonlRoadReportVersionEncoder
import io.cruvelo.operaty.report.road.RoadReportPdfContentRepository
import io.cruvelo.operaty.report.road.RoadReportRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID

private val LOGGER = KotlinLogging.logger {}

class RoadReportFineTuner(
	private val fineTunerSourceProvider: FineTunerSourceProvider,
	private val roadReportRepository: RoadReportRepository,
	private val roadReportPdfContentRepository: RoadReportPdfContentRepository,
	private val jsonlRoadReportVersionEncoder: JsonlRoadReportVersionEncoder,
	private val jsonlFineTuneEncoder: JsonlFineTuneEncoder,
	private val openAiFileUploader: OpenAiFileUploader,
	private val openAiFineTuningHttp: OpenAiFineTuningHttp,
	private val fineTunedModelJsonlFileReferenceRepository: FineTunedModelJsonlFileReferenceRepository,
	private val fineTunedModelRepository: FineTunedModelRepository,
) {

	suspend fun fineTune(): FineTunedModel {
		val source = fineTunerSourceProvider.provide()
		val stringBuilder = StringBuilder()
		source.forEach { (roadReportId, version) ->
			val roadReport = roadReportRepository.findByIdOrThrow(roadReportId)
			val version = roadReport.versions.singleOrNull { it.version == version }
				?: error("Could not generate fine tuning data. Road report version $version not found for road report $roadReportId")
			val roadReportPdfContent = roadReportPdfContentRepository.findContentByIdOrThrow(roadReportId)
			val fineTuneJsonl = FineTuningJsonl(
				listOf(
					FineTuningJsonl.ContentMessage(FineTuningJsonl.Role.USER, roadReportPdfContent),
					FineTuningJsonl.ContentMessage(FineTuningJsonl.Role.ASSISTANT, jsonlRoadReportVersionEncoder.encode(version)),
				)
			)
			val jsonl = jsonlFineTuneEncoder.encode(fineTuneJsonl)
			stringBuilder.append(jsonl)
			stringBuilder.append("\n")
		}
		val fileUpload = OpenAiFileUploader.UploadFile(
			file = stringBuilder.toString().byteInputStream(),
			purpose = "fine-tune-data",
			filename = "${UUID.randomUUID()}.jsonl",
		)
		val fileId = openAiFileUploader.upload(fileUpload)

		val response = openAiFineTuningHttp.createFineTuningJob(
			OpenAiFineTuningHttp.CreateFineTuningJobRequest(
				trainingFile = fileId.value,
				model = "gpt-4.1-2025-04-14"
			)
		)

		val model = FineTunedModel(
			providerId = response.id,
			source = source
		).let(fineTunedModelRepository::save).also {
			fineTunedModelJsonlFileReferenceRepository.save(
				it.id.toString(),
				fileId.value
			)
		}

		return model
	}
}
