package io.cruvelo.operaty.openai

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.InputStream

object Schemas {

	val ROAD_REPORT_OUTPUT_JSON_SCHEMA: JsonObject by lazy {
		val jsonSchemaString = javaClass.classLoader.getResourceAsStream("report_road.json")
			?.use { inputStream: InputStream -> inputStream.buffered().readAllBytes() }
			?.toString(Charsets.UTF_8)
			?: error("Failed to load road report output JSON schema")
		return@lazy Json.decodeFromString<JsonObject>(jsonSchemaString)
	}
}
