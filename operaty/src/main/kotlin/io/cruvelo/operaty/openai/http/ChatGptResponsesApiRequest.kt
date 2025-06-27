package io.cruvelo.operaty.openai.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ChatGptResponsesApiRequest(
	val model: String,
	val prompt: Prompt,
	val input: Set<Input>,
	val text: Text,
) {

	@Serializable
	data class Prompt(
		val id: String,
		val version: String
	)

	@Serializable
	data class Input(
		val role: String,
		val content: Set<Content>
	) {

		@Serializable
		data class Content(
			val type: String,
			@SerialName("file_id")
			val fileId: String? = null
		)
	}

	@Serializable
	data class Text(
		val format: Format
	) {
		@Serializable
		data class Format(
			val name: String,
			val schema: JsonObject,
			val description: String,
			val type: String
		)
	}
}
