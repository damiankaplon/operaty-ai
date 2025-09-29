package io.cruvelo.operaty.openai.http

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ChatGptEvalsApiRequest(
	val name: String? = null,
	@SerialName("data_source_config")
	val dataSourceConfig: DataSourceConfig,
	@SerialName("testing_criteria")
	val testingCriteria: List<PythonGrader>,
	val metadata: Metadata? = null,
) {

	@Serializable
	data class Message(
		val role: String,
		val content: String,
	)

	@Serializable
	data class Metadata(
		val description: String? = null,
	)

	@Serializable
	data class DataSourceConfig(
		val type: String,
		@SerialName("item_schema")
		val itemSchema: Schema? = null,
	) {

		@Serializable
		data class Schema(
			val type: String,
			val properties: Map<String, SchemaProperty>,
			val required: List<String>?,
		)

		@Serializable
		data class SchemaProperty(
			val type: JsonElement,
			val properties: Map<String, SchemaProperty>?,
			val required: List<String>?,
		)
	}

	@OptIn(ExperimentalSerializationApi::class)
	@Serializable
	data class PythonGrader(
		val name: String,
		val source: String,
		@SerialName("pass_threshold")
		val passThreshold: Float,
		@EncodeDefault val type: String = "python",
	)
}
