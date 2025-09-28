package io.cruvelo.operaty.openai.finetuning

fun interface JsonlFineTuneEncoder {

	fun encode(value: FineTuningJsonl): String
}

object JsonlFineTuneEncoderKotlinx : JsonlFineTuneEncoder {

	private val JSON = kotlinx.serialization.json.Json { ignoreUnknownKeys = true; prettyPrint = false; classDiscriminator = "kotlinx_type" }

	override fun encode(value: FineTuningJsonl): String {
		return JSON.encodeToString(value)
	}
}
