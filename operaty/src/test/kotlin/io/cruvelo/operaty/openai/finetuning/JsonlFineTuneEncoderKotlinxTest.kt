package io.cruvelo.operaty.openai.finetuning

import io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl
import io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl.ContentMessage
import io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl.Role
import io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl.ToolCallsMessage
import io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl.ToolCallsMessage.ToolCall
import io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl.ToolCallsMessage.ToolCall.Function
import io.cruvelo.operaty.openai.finetuning.infra.JsonlFineTuneEncoderKotlinx
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class JsonlFineTuneEncoderKotlinxTest {

	@Test
	fun `should properly encode object to single line jsonl`() {
		// given
		val operatyPdfContent = "Operat text which is very long and \n contains new lines as well \n crazy"
		val value = FineTuningJsonl(
			listOf(
				ContentMessage(Role.USER, operatyPdfContent),
				ToolCallsMessage(
					Role.ASSISTANT, listOf(
						ToolCall(
							"1",
							"function",
							Function(
								"functionName",
								"{\"content\": \"$operatyPdfContent\"}"
							)
						)
					)
				)
			)
		)

		// when
		val result = JsonlFineTuneEncoderKotlinx.encode(value)

		// then
		val expectedJson =
			"""{"messages":[{"kotlinx_type":"io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl.ContentMessage","role":"user","content":"Operat text which is very long and \n contains new lines as well \n crazy"},{"kotlinx_type":"io.cruvelo.operaty.openai.finetuning.infra.FineTuningJsonl.ToolCallsMessage","role":"assistant","tool_calls":[{"id":"1","type":"function","function":{"name":"functionName","arguments":"{\"content\": \"Operat text which is very long and \n contains new lines as well \n crazy\"}"}}]}]}"""
		assertThat(result).isEqualTo(expectedJson)
	}
}