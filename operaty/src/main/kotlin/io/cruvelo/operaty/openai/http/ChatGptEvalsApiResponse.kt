package io.cruvelo.operaty.openai.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatGptEvalsApiResponse(
    val id: String,
    val status: String,
    @SerialName("created_at")
    val createdAt: Long,
    val metadata: Metadata? = null
) {
    @Serializable
    data class Metadata(
        val description: String? = null
    )
}