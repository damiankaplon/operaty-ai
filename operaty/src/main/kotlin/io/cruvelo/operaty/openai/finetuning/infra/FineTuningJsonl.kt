package io.cruvelo.operaty.openai.finetuning.infra

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FineTuningJsonl(
	val messages: List<Message>,
) {

	@Serializable
	sealed class Message {

		abstract val role: Role
	}

	@Serializable
	data class ContentMessage(
		override val role: Role,
		val content: String,
	) : Message()

	@Serializable
	data class ToolCallsMessage(
		override val role: Role,
		@SerialName("tool_calls")
		val toolCalls: List<ToolCall>,
	) : Message() {

		@Serializable
		data class ToolCall(
			val id: String,
			val type: String,
			val function: Function,
		) {

			@Serializable
			data class Function(
				val name: String,
				val arguments: String,
			)
		}
	}

	enum class Role {
		@SerialName("user")
		USER,

		@SerialName("assistant")
		ASSISTANT
	}
}