package io.cruvelo.operaty.openai.finetuning

import io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl
import io.cruvelo.operaty.report.road.RoadReport

fun interface JsonlFineTuneEncoder {

	fun encode(value: FineTuningJsonl): String
}

fun interface JsonlRoadReportVersionEncoder {

	fun encode(value: RoadReport.Version): String
}
