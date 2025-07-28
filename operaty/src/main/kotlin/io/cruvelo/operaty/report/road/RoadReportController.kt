package io.cruvelo.operaty.report.road

import io.cruvelo.operaty.db.TransactionalRunner
import io.cruvelo.operaty.openai.Schemas
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiResponse
import io.cruvelo.operaty.openai.http.ChatGptRoadReportResponse
import io.cruvelo.operaty.report.road.RoadReportVersionTable.area
import io.cruvelo.operaty.report.road.RoadReportVersionTable.bank
import io.cruvelo.operaty.report.road.RoadReportVersionTable.columns
import io.cruvelo.operaty.report.road.RoadReportVersionTable.demolition
import io.cruvelo.operaty.report.road.RoadReportVersionTable.detailed
import io.cruvelo.operaty.report.road.RoadReportVersionTable.dig
import io.cruvelo.operaty.report.road.RoadReportVersionTable.ditch
import io.cruvelo.operaty.report.road.RoadReportVersionTable.excavation
import io.cruvelo.operaty.report.road.RoadReportVersionTable.flat
import io.cruvelo.operaty.report.road.RoadReportVersionTable.from
import io.cruvelo.operaty.report.road.RoadReportVersionTable.id
import io.cruvelo.operaty.report.road.RoadReportVersionTable.inOut
import io.cruvelo.operaty.report.road.RoadReportVersionTable.infill
import io.cruvelo.operaty.report.road.RoadReportVersionTable.inner
import io.cruvelo.operaty.report.road.RoadReportVersionTable.length
import io.cruvelo.operaty.report.road.RoadReportVersionTable.loweredCurb
import io.cruvelo.operaty.report.road.RoadReportVersionTable.measurementDate
import io.cruvelo.operaty.report.road.RoadReportVersionTable.odh
import io.cruvelo.operaty.report.road.RoadReportVersionTable.pa
import io.cruvelo.operaty.report.road.RoadReportVersionTable.report
import io.cruvelo.operaty.report.road.RoadReportVersionTable.reportDate
import io.cruvelo.operaty.report.road.RoadReportVersionTable.reportNumber
import io.cruvelo.operaty.report.road.RoadReportVersionTable.rim
import io.cruvelo.operaty.report.road.RoadReportVersionTable.roadNumber
import io.cruvelo.operaty.report.road.RoadReportVersionTable.slope
import io.cruvelo.operaty.report.road.RoadReportVersionTable.surface
import io.cruvelo.operaty.report.road.RoadReportVersionTable.task
import io.cruvelo.operaty.report.road.RoadReportVersionTable.to
import io.cruvelo.operaty.report.road.RoadReportVersionTable.version
import io.cruvelo.operaty.report.road.RoadReportVersionTable.volume
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
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

class RoadReportController(
	private val transactionalRunner: TransactionalRunner,
	chatGptHttpClient: ChatGptHttpClient,
	private val json: Json,
) {

	private val chatGptHttpClient = chatGptHttpClient.client

	suspend fun generate(multipart: MultiPartData): RoadReport {
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
			RoadReportVersionTable.insert { it ->
				it.from(roadReport)
				it[version] = 0
			}
		}
		return roadReport
	}

	suspend fun update(roadReport: RoadReport): RoadReport = transactionalRunner.transaction {
		val versionMax = version.max()
		val recentVersion = RoadReportVersionTable.select(versionMax).where { id eq roadReport.id }.map { it[versionMax] }.single()
		RoadReportVersionTable.insert {
			it.from(roadReport)
			it[version] = recentVersion?.plus(1) ?: 0
		}
		return@transaction RoadReportVersionTable.select(columns).where { id eq roadReport.id }
			.maxBy { it[version] }
			.toRoadReport()
	}

	suspend fun getAll(): Set<RoadReport> = transactionalRunner.transaction(readOnly = true) {
		return@transaction RoadReportVersionTable.select(columns)
			.groupBy { it[reportNumber] }
			.mapTo(linkedSetOf()) { (_, rows) -> rows.maxBy { it[version] } }
			.mapTo(linkedSetOf()) { it.toRoadReport() }
	}
}

private fun ResultRow.toRoadReport() = RoadReport(
	id = this[id].value,
	reportNumber = this[reportNumber],
	area = this[area],
	roadNumber = this[roadNumber],
	from = this[from],
	to = this[to],
	detailed = this[detailed],
	task = this[task],
	report = this[report],
	measurementDate = this[measurementDate],
	reportDate = this[reportDate],
	length = this[length],
	loweredCurb = this[loweredCurb],
	rim = this[rim],
	inOut = this[inOut],
	flat = this[flat],
	pa = this[pa],
	slope = this[slope],
	ditch = this[ditch],
	demolition = this[demolition],
	surface = this[surface],
	volume = this[volume],
	inner = this[inner],
	odh = this[odh],
	dig = this[dig],
	infill = this[infill],
	bank = this[bank],
	excavation = this[excavation],
)

private object RoadReportVersionTable : UUIDTable("report_road_version") {

	val reportNumber = integer("report_number")
	val version = integer("version")
	val area = text("area").nullable()
	val roadNumber = text("road_number").nullable()
	val from = text("from").nullable()
	val to = text("to").nullable()
	val detailed = text("detailed").nullable()
	val task = text("task").nullable()
	val report = text("report").nullable()
	val measurementDate = text("measurement_date").nullable()
	val reportDate = text("report_date").nullable()
	val length = text("length").nullable()
	val loweredCurb = text("lowered_curb").nullable()
	val rim = text("rim").nullable()
	val inOut = text("in_out").nullable()
	val flat = text("flat").nullable()
	val pa = text("pa").nullable()
	val slope = text("slope").nullable()
	val ditch = text("ditch").nullable()
	val demolition = text("demolition").nullable()
	val surface = text("surface").nullable()
	val volume = text("volume").nullable()
	val inner = text("inner").nullable()
	val odh = text("odh").nullable()
	val dig = float("dig").nullable()
	val infill = float("infill").nullable()
	val bank = float("bank").nullable()
	val excavation = float("excavation").nullable()

	fun <T : Any> InsertStatement<T>.from(roadReport: RoadReport) {
		this[id] = roadReport.id
		this[reportNumber] = roadReport.reportNumber
		this[area] = roadReport.area
		this[from] = roadReport.from
		this[to] = roadReport.to
		this[detailed] = roadReport.detailed
		this[task] = roadReport.task
		this[report] = roadReport.report
		this[measurementDate] = roadReport.measurementDate
		this[reportDate] = roadReport.reportDate
		this[length] = roadReport.length
		this[loweredCurb] = roadReport.loweredCurb
		this[rim] = roadReport.rim
		this[inOut] = roadReport.inOut
		this[flat] = roadReport.flat
		this[pa] = roadReport.pa
		this[slope] = roadReport.slope
		this[ditch] = roadReport.ditch
		this[demolition] = roadReport.demolition
		this[surface] = roadReport.surface
		this[volume] = roadReport.volume
		this[inner] = roadReport.inner
		this[odh] = roadReport.odh
		this[dig] = roadReport.dig
		this[infill] = roadReport.infill
		this[bank] = roadReport.bank
		this[excavation] = roadReport.excavation
	}
}

private fun roadReportFrom(chatGptRoadReportResponse: ChatGptRoadReportResponse): RoadReport =
	with(chatGptRoadReportResponse) {
		RoadReport(
			UUID.randomUUID(),
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
	}
