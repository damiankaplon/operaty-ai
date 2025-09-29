package io.cruvelo.operaty.openai.finetuning.infra

import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class OpenAiFineTuningHttp(
	chatGptHttpClient: ChatGptHttpClient,
) {

	private val httpClient = chatGptHttpClient.client

	suspend fun createFineTuningJob(createFineTuningJobRequest: CreateFineTuningJobRequest) =
		httpClient.post {
			url { path("/v1/fine_tuning/jobs") }
			setBody(createFineTuningJobRequest)
		}.body<FineTuningJobResponse>()

	suspend fun getFineTuningJob(fineTuningJobId: String) = httpClient.get {
		url { path("/v1/fine_tuning/jobs/$fineTuningJobId") }
	}.body<FineTuningJobResponse>()

	@Serializable
	data class CreateFineTuningJobRequest(
		@SerialName("training_file")
		val trainingFile: String,
		val model: String,
	)

	@Serializable
	data class FineTuningJobResponse(
		val id: String,
	)
}
