package io.cruvelo.operaty.openai.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
			val type: String = "object",
			val properties: Map<String, SchemaProperty>,
			val required: List<String>? = null,
		)

		@Serializable
		data class SchemaProperty(
			val type: String = "string",
			val properties: Map<String, SchemaProperty>? = null,
			val required: List<String>? = null,
		)
	}

 @Serializable
 data class PythonGrader(
 	val name: String,
	val source: String,
 	val type: String = "python",
	@SerialName("pass_threshold")
 	val passThreshold: Float,
 )
}