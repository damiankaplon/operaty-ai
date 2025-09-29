package io.cruvelo.operaty.openai.evals

import io.cruvelo.operaty.report.road.RoadReport
import io.cruvelo.operaty.report.road.RoadReportPdfContentRepository
import io.cruvelo.operaty.report.road.RoadReportRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.util.UUID

fun interface JsonlFileEvalRunFactory {

	fun create(roadReportId: UUID): String
}

class DefaultJsonlFileEvalRunFactory(
	private val json: Json,
	private val roadReportRepository: RoadReportRepository,
	private val roadReportPdfContentRepository: RoadReportPdfContentRepository,
) : JsonlFileEvalRunFactory {

	override fun create(roadReportId: UUID): String {
		val roadReport = roadReportRepository.findByIdOrThrow(roadReportId)
		val expectedResult = roadReport.toExpectedResult()
		val content = roadReportPdfContentRepository.findContentByIdOrThrow(roadReportId)
		val expectedResultJson = json.encodeToJsonElement(expectedResult)
		val jsonl = Jsonl(content, expectedResultJson)
		return json.encodeToString(jsonl)
	}

	private fun RoadReport.toExpectedResult(): ExpectedResult {
		val currentVersion = this.versions.maxBy(RoadReport.Version::version)
		return ExpectedResult(
			reportNumber = currentVersion.content.reportNumber,
			area = currentVersion.content.area,
			roadNumber = currentVersion.content.roadNumber,
			from = currentVersion.content.from,
			to = currentVersion.content.to,
			detailed = currentVersion.content.detailed,
			task = currentVersion.content.task,
			report = currentVersion.content.report,
			measurementDate = currentVersion.content.measurementDate,
			reportDate = currentVersion.content.reportDate,
			length = currentVersion.content.length,
			loweredCurb = currentVersion.content.loweredCurb,
			rim = currentVersion.content.rim,
			inOut = currentVersion.content.inOut,
			flat = currentVersion.content.flat,
			pa = currentVersion.content.pa,
			slope = currentVersion.content.slope,
			ditch = currentVersion.content.ditch,
			demolition = currentVersion.content.demolition,
			surface = currentVersion.content.surface,
			volume = currentVersion.content.volume,
			inner = currentVersion.content.inner,
			odh = currentVersion.content.odh,
			dig = currentVersion.content.dig,
			infill = currentVersion.content.infill,
			bank = currentVersion.content.bank,
			excavation = currentVersion.content.excavation,
		)
	}
}

@Serializable
private data class Jsonl(
	@SerialName("road_report_pdf_content")
	val content: String,
	@SerialName("expected_output")
	val expected: JsonElement,
)

@Serializable
private data class ExpectedResult(
	val reportNumber: Int,
	val area: String?,
	val roadNumber: String?,
	val from: String?,
	val to: String?,
	val detailed: String?,
	val task: String?,
	val report: String?,
	val measurementDate: String?,
	val reportDate: String?,
	val length: String?,
	val loweredCurb: String?,
	val rim: String?,
	val inOut: String?,
	val flat: String?,
	val pa: String?,
	val slope: String?,
	val ditch: String?,
	val demolition: String?,
	val surface: String?,
	val volume: String?,
	val inner: String?,
	val odh: String?,
	val dig: Float?,
	val infill: Float?,
	val bank: Float?,
	val excavation: Float?,
)