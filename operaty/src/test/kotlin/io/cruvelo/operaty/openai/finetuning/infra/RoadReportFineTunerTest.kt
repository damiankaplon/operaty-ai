package io.cruvelo.operaty.openai.finetuning.infra

import io.cruvelo.operaty.openai.files.OpenAiFileUploader
import io.cruvelo.operaty.openai.finetuning.FineTunedModel
import io.cruvelo.operaty.openai.finetuning.FineTunedModelRepository
import io.cruvelo.operaty.openai.finetuning.FineTunerSourceProvider
import io.cruvelo.operaty.openai.finetuning.JsonlFineTuneEncoder
import io.cruvelo.operaty.openai.finetuning.JsonlRoadReportVersionEncoder
import io.cruvelo.operaty.openai.finetuning.RoadReportStub
import io.cruvelo.operaty.openai.finetuning.RoadReportVersionStub
import io.cruvelo.operaty.report.road.RoadReportPdfContentRepository
import io.cruvelo.operaty.report.road.RoadReportRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RoadReportFineTunerTest {

	@RelaxedMockK
	private lateinit var fineTunerSourceProvider: FineTunerSourceProvider

	@RelaxedMockK
	private lateinit var roadReportRepository: RoadReportRepository

	@RelaxedMockK
	private lateinit var roadReportPdfContentRepository: RoadReportPdfContentRepository

	@RelaxedMockK
	private lateinit var jsonlRoadReportVersionEncoder: JsonlRoadReportVersionEncoder

	@RelaxedMockK
	private lateinit var jsonlFineTuneEncoder: JsonlFineTuneEncoder

	@RelaxedMockK
	private lateinit var openAiFileUploader: OpenAiFileUploader

	@RelaxedMockK
	private lateinit var openAiFineTuningHttp: OpenAiFineTuningHttp

	@RelaxedMockK
	private lateinit var fineTunedModelJsonlFileReferenceRepository: FineTunedModelJsonlFileReferenceRepository

	@RelaxedMockK
	private lateinit var fineTunedModelRepository: FineTunedModelRepository

	private lateinit var roadReportFineTuner: RoadReportFineTuner

	@BeforeTest
	fun setUp() {
		MockKAnnotations.init(this)
		roadReportFineTuner = RoadReportFineTuner(
			fineTunerSourceProvider,
			roadReportRepository,
			roadReportPdfContentRepository,
			jsonlRoadReportVersionEncoder,
			jsonlFineTuneEncoder,
			openAiFileUploader,
			openAiFineTuningHttp,
			fineTunedModelJsonlFileReferenceRepository,
			fineTunedModelRepository
		)
	}

	@Test
	fun `should use all sources from provider`(): Unit = runBlocking {
		// given
		val source1Id = UUID.randomUUID()
		val source2Id = UUID.randomUUID()
		val sources = setOf(
			FineTunedModel.Source(source1Id, 1),
			FineTunedModel.Source(source2Id, 2)
		)
		every { fineTunerSourceProvider.provide() } returns sources

		val report1 = RoadReportStub { id = source1Id; versions = setOf(RoadReportVersionStub { version = 1 }) }
		val report2 = RoadReportStub { id = source2Id; versions = setOf(RoadReportVersionStub { version = 2 }) }
		every { roadReportRepository.findByIdOrThrow(source1Id) } returns report1
		every { roadReportRepository.findByIdOrThrow(source2Id) } returns report2

		every { roadReportPdfContentRepository.findContentByIdOrThrow(any()) } returns "pdf content"
		every { jsonlRoadReportVersionEncoder.encode(any()) } returns "encoded version"
		every { jsonlFineTuneEncoder.encode(any()) } returns "encoded jsonl"

		coEvery { openAiFileUploader.upload(any()) } returns OpenAiFileUploader.FileId("file-id")
		coEvery { openAiFineTuningHttp.createFineTuningJob(any()) } returns OpenAiFineTuningHttp.FineTuningJobResponse("job-id")

		val fineTunedModelSlot = slot<FineTunedModel>()
		every { fineTunedModelRepository.save(capture(fineTunedModelSlot)) } answers { fineTunedModelSlot.captured }

		// when
		val result = roadReportFineTuner.fineTune()

		// then
		coVerify(exactly = 1) { roadReportRepository.findByIdOrThrow(source1Id) }
		coVerify(exactly = 1) { roadReportRepository.findByIdOrThrow(source2Id) }

		val uploadFileSlot = slot<OpenAiFileUploader.UploadFile>()
		coVerify { openAiFileUploader.upload(capture(uploadFileSlot)) }
		val content = uploadFileSlot.captured.file.reader().readText().trim()
		val lines = content.split('\n')
		assertThat(lines).hasSize(2)
		assertThat(lines).allMatch { it == "encoded jsonl" }


		assertThat(fineTunedModelSlot.captured.source).isEqualTo(sources)
		assertThat(result).isEqualTo(fineTunedModelSlot.captured)
	}
}
