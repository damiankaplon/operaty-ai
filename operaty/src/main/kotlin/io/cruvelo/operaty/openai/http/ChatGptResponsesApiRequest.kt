package io.cruvelo.operaty.openai.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ChatGptResponsesApiRequest(
	val model: String? = null,
	val prompt: Prompt,
	val input: Set<ContentInput>,
	val text: Text,
) {

	@Serializable
	data class Prompt(
		val id: String,
		val version: String
	)

	@Serializable
	data class ContentInput(
		val role: String,
		val content: Set<Content>
	) {

		@Serializable
		data class Content(
			val type: String,
			@SerialName("file_id")
			val fileId: String? = null,
			val text: String? = null
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
