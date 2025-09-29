package io.cruvelo.operaty.openai.finetuning.infra

import io.cruvelo.operaty.openai.finetuning.JsonlFineTuneEncoder
import io.cruvelo.operaty.openai.finetuning.JsonlRoadReportVersionEncoder
import io.cruvelo.operaty.report.road.RoadReport
import kotlinx.serialization.json.Json

object JsonlFineTuneEncoderKotlinx : JsonlFineTuneEncoder, JsonlRoadReportVersionEncoder {

	private val JSON = Json { ignoreUnknownKeys = true; prettyPrint = false; classDiscriminator = "kotlinx_type" }

	override fun encode(value: FineTuningJsonl): String {
		return JSON.encodeToString(value)
	}

	override fun encode(value: RoadReport.Version): String {
		return JSON.encodeToString(value)
	}
}
