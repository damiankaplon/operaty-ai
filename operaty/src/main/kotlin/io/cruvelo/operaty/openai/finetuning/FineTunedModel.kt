package io.cruvelo.operaty.openai.finetuning

import io.cruvelo.kotlinx.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

class FineTunedModel(
	val id: UUID,
	val providerId: String,
	val source: Set<Source>,
) {

	@Serializable
	data class Source(
		@Serializable(UUIDSerializer::class) val roadReportId: UUID,
		val version: Int,
	)

	var accuracy: Float? = null
		set(value) {
			require(value != null && value in 0.0..1.0) { "Accuracy must be between 0 and 1" }
			field = value
		}
}
