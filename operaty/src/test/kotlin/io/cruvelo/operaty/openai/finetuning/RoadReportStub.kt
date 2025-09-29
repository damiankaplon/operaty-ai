package io.cruvelo.operaty.openai.finetuning

import io.cruvelo.operaty.report.road.RoadReport
import java.util.UUID

object RoadReportStub {

	data class RoadReportBuilder(
		var id: UUID = UUID.randomUUID(),
		var versions: Set<RoadReport.Version> = setOf(RoadReportVersionStub {}),
	)

	operator fun invoke(adjust: RoadReportBuilder.() -> Unit): RoadReport {
		val builder = RoadReportBuilder()
		builder.adjust()
		return RoadReport(
			id = builder.id,
			versions = builder.versions
		)
	}
}

object RoadReportVersionStub {

	data class RoadReportVersionBuilder(
		var version: Int = 0,
		var reportNumber: Int = 2025,
		var area: String? = null,
		var roadNumber: String? = null,
		var from: String? = null,
		var to: String? = null,
		var detailed: String? = null,
		var task: String? = null,
		var report: String? = null,
		var measurementDate: String? = null,
		var reportDate: String? = null,
		var length: String? = null,
		var loweredCurb: String? = null,
		var rim: String? = null,
		var inOut: String? = null,
		var flat: String? = null,
		var pa: String? = null,
		var slope: String? = null,
		var ditch: String? = null,
		var demolition: String? = null,
		var surface: String? = null,
		var volume: String? = null,
		var inner: String? = null,
		var odh: String? = null,
		var dig: Float? = null,
		var infill: Float? = null,
		var bank: Float? = null,
		var excavation: Float? = null,
	)

	operator fun invoke(adjust: RoadReportVersionBuilder.() -> Unit): RoadReport.Version {
		val builder = RoadReportVersionBuilder().apply(adjust)
		return with(builder) {
			RoadReport.Version(
				version,
				RoadReport.Version.Content(
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
					excavation
				)
			)
		}
	}
}
