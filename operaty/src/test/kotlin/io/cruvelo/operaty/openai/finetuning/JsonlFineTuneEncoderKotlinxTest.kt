package io.cruvelo.operaty.openai.finetuning

import io.cruvelo.operaty.openai.finetuning.FineTuningJsonl.ContentMessage
import io.cruvelo.operaty.openai.finetuning.FineTuningJsonl.Role
import io.cruvelo.operaty.openai.finetuning.FineTuningJsonl.ToolCallsMessage
import io.cruvelo.operaty.openai.finetuning.FineTuningJsonl.ToolCallsMessage.ToolCall
import io.cruvelo.operaty.openai.finetuning.FineTuningJsonl.ToolCallsMessage.ToolCall.Function
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class JsonlFineTuneEncoderKotlinxTest {

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

		assertThat(result).isEqualTo("""{"messages":[{"kotlinx_type":"io.cruvelo.operaty.openai.finetuning.FineTuningJsonl.ContentMessage","role":"user","content":"Operat text which is very long and \n contains new lines as well \n crazy"},{"kotlinx_type":"io.cruvelo.operaty.openai.finetuning.FineTuningJsonl.ToolCallsMessage","role":"assistant","tool_calls":[{"id":"1","type":"function","function":{"name":"functionName","arguments":"{\"content\": \"Operat text which is very long and \n contains new lines as well \n crazy\"}"}}]}]}""")
	}
}
