package io.cruvelo.operaty.openai.http

import kotlinx.serialization.Serializable

@Serializable
data class ChatGptEval(
	val id: String,
	val name: String,
)
