package io.cruvelo.operaty.report.road

import kotlin.random.Random

object ContentStub {

	class Builder(
		var reportNumber: Int = Random.nextInt(),
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

	operator fun invoke(adjust: Builder.() -> Unit): RoadReport.Version.Content {
		val builder = Builder()
		builder.adjust()
		return RoadReport.Version.Content(
			reportNumber = builder.reportNumber,
			area = builder.area,
			roadNumber = builder.roadNumber,
			from = builder.from,
			to = builder.to,
			detailed = builder.detailed,
			task = builder.task,
			report = builder.report,
			measurementDate = builder.measurementDate,
			reportDate = builder.reportDate,
			length = builder.length,
			loweredCurb = builder.loweredCurb,
			rim = builder.rim,
			inOut = builder.inOut,
			flat = builder.flat,
			pa = builder.pa,
			slope = builder.slope,
			ditch = builder.ditch,
			demolition = builder.demolition,
			surface = builder.surface,
			volume = builder.volume,
			inner = builder.inner,
			odh = builder.odh,
			dig = builder.dig,
			infill = builder.infill,
			bank = builder.bank,
			excavation = builder.excavation,
		)
	}
}
