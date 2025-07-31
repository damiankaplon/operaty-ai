package io.cruvelo.operaty.report.road

import io.cruvelo.operaty.db.TransactionalRunner
import io.cruvelo.operaty.openai.Schemas
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiResponse
import io.cruvelo.operaty.openai.http.ChatGptRoadReportResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json
import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.util.UUID

class RoadReportController(
	private val roadReportRepository: RoadReportRepository,
	private val roadReportPdfContentRepository: RoadReportPdfContentRepository,
	private val transactionalRunner: TransactionalRunner,
	chatGptHttpClient: ChatGptHttpClient,
	private val json: Json,
) {

	private val chatGptHttpClient = chatGptHttpClient.client

	suspend fun generate(multipart: MultiPartData): RoadReportVersionDto {
		var fileBytes: ByteArray = byteArrayOf()

		multipart.forEachPart { part: PartData ->
			when (part) {
				is PartData.FileItem -> {
					val channel: ByteReadChannel = part.provider()
					fileBytes = fileBytes.plus(channel.readRemaining().readByteArray())
				}
				else -> {}
			}
			part.dispose()
		}

		val pdf = Loader.loadPDF(fileBytes)
		val textStripper = PDFTextStripper().apply {
			sortByPosition = true
		}
		val pdfText = textStripper.getText(pdf)

		val response: ChatGptResponsesApiResponse = chatGptHttpClient.post {
			url { path("v1/responses") }
			contentType(Json)
			setBody(
				ChatGptResponsesApiRequest(
					prompt = ChatGptResponsesApiRequest.Prompt(
						id = "pmpt_685c304658a081978b7633929d9785f702284f6f4e91ded8",
						version = "9"
					),
					input = setOf(
						ChatGptResponsesApiRequest.ContentInput(
							role = "user",
							content = setOf(
								ChatGptResponsesApiRequest.ContentInput.Content(
									type = "input_text",
									text = pdfText
								)
							)
						)
					),
					text = ChatGptResponsesApiRequest.Text(
						format = ChatGptResponsesApiRequest.Text.Format(
							name = "text",
							type = "json_schema",
							schema = Schemas.ROAD_REPORT_OUTPUT_JSON_SCHEMA,
							description = "Extracts the road report data from the PDF file"
						)
					)
				)
			)
		}.body<ChatGptResponsesApiResponse>()
		val roadReport: RoadReport = json.decodeFromString<ChatGptRoadReportResponse>(response.output.first().content.first().text)
			.let(::roadReportFrom)
		transactionalRunner.transaction {
			roadReportRepository.save(roadReport)
			roadReportPdfContentRepository.save(roadReport.id, pdfText)
		}
		return roadReport.toDto().single()
	}

	suspend fun update(roadReportDto: RoadReportDto): Set<RoadReportVersionDto> = transactionalRunner.transaction {
		val roadReport = roadReportRepository.findByIdOrThrow(roadReportDto.id)
		val newVersion: RoadReport.Version.Content = roadReportDto.toNewVersion()
		roadReport.newVersion(newVersion)
		roadReportRepository.save(roadReport)
		return@transaction roadReport.toDto()
	}

	suspend fun getAll(): Set<RoadReportVersionDto> = transactionalRunner.transaction(readOnly = true) {
		return@transaction roadReportRepository.findAll().flatMap { it.toDto() }.toSet()
	}
}

private fun RoadReportDto.toNewVersion(): RoadReport.Version.Content {
	return RoadReport.Version.Content(
		reportNumber = reportNumber,
		area = area,
		roadNumber = roadNumber,
		from = from,
		to = to,
		detailed = detailed,
		task = task,
		report = report,
		measurementDate = measurementDate,
		reportDate = reportDate,
		length = length,
		loweredCurb = loweredCurb,
		rim = rim,
		inOut = inOut,
		flat = flat,
		pa = pa,
		slope = slope,
		ditch = ditch,
		demolition = demolition,
		surface = surface,
		volume = volume,
		inner = inner,
		odh = odh,
		dig = dig,
		infill = infill,
		bank = bank,
		excavation = excavation
	)
}

private fun roadReportFrom(chatGptRoadReportResponse: ChatGptRoadReportResponse): RoadReport =
	with(chatGptRoadReportResponse) {
		RoadReport(
			id = UUID.randomUUID(),
			initialVersion = RoadReport.Version.Content(
				reportNumber,
				area,
				roadNumber,
				from,
				to,
				detailed,
				task,
				report,
				measurementDate,
				reportDate,
				length,
				loweredCurb,
				rim,
				inOut,
				flat,
				pa,
				slope,
				ditch,
				demolition,
				surface,
				volume,
				inner,
				odh,
				dig,
				infill,
				bank,
				excavation,
			)
		)
	}

fun RoadReport.toDto(): Set<RoadReportVersionDto> = this.versions.mapTo(linkedSetOf()) {
	RoadReportVersionDto(
		id = this.id,
		version = it.version,
		reportNumber = it.content.reportNumber,
		area = it.content.area,
		roadNumber = it.content.roadNumber,
		from = it.content.from,
		to = it.content.to,
		detailed = it.content.detailed,
		task = it.content.task,
		report = it.content.report,
		measurementDate = it.content.measurementDate,
		reportDate = it.content.reportDate,
		length = it.content.length,
		loweredCurb = it.content.loweredCurb,
		rim = it.content.rim,
		inOut = it.content.inOut,
		flat = it.content.flat,
		pa = it.content.pa,
		slope = it.content.slope,
		ditch = it.content.ditch,
		demolition = it.content.demolition,
		surface = it.content.surface,
		volume = it.content.volume,
		inner = it.content.inner,
		odh = it.content.odh,
		dig = it.content.dig,
		infill = it.content.infill,
		bank = it.content.bank,
		excavation = it.content.excavation,
	)
}
