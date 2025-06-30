package io.cruvelo.operaty.openai.http

import kotlinx.serialization.Serializable

@Serializable
data class ChatGptResponsesApiResponse(
	val output: Set<Output>
) {
	@Serializable
	data class Output(
		val content: Set<Content>,
	) {
		@Serializable
		data class Content(
			val text: String,
		)
	}
}
