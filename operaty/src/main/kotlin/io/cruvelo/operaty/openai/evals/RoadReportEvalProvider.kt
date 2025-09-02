package io.cruvelo.operaty.openai.evals

import io.cruvelo.operaty.openai.Schemas.ROAD_REPORT_OUTPUT_JSON_SCHEMA
import io.cruvelo.operaty.openai.http.ChatGptEval
import io.cruvelo.operaty.openai.http.ChatGptEvalsApiRequest
import io.cruvelo.operaty.openai.http.ChatGptEvalsApiRequest.DataSourceConfig
import io.cruvelo.operaty.openai.http.ChatGptEvalsApiRequest.DataSourceConfig.Schema
import io.cruvelo.operaty.openai.http.ChatGptEvalsApiRequest.DataSourceConfig.SchemaProperty
import io.cruvelo.operaty.openai.http.ChatGptEvalsApiRequest.Metadata
import io.cruvelo.operaty.openai.http.ChatGptEvalsResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement

private val LOGGER = KotlinLogging.logger { }

class RoadReportEvalProvider(
	private val properties: Properties,
	private val pythonScriptGraderProvider: PythonScriptGraderProvider,
	private val chatGptHttpClient: HttpClient,
	private val json: Json,
) {

	data class Properties(
		val name: String,
		val pythonGraderSrc: String,
	)

	suspend fun provide(): String {
		LOGGER.debug { "Finding a road report eval with name: ${properties.name} in existing evals" }
		val evals = chatGptHttpClient.get { url { path("v1/evals") } }.body<ChatGptEvalsResponse>()
		return evals.data.firstOrNull { it.name == properties.name }?.id
			?: createEval()
	}

	private suspend fun createEval(): String {
		LOGGER.debug { "Creating a road report eval with name: ${properties.name}" }
		val evalRequest = ChatGptEvalsApiRequest(
			name = properties.name,
			dataSourceConfig = DataSourceConfig(
				type = "custom",
				itemSchema = Schema(
					type = "object",
					properties = mapOf(
						"item" to SchemaProperty(
							type = JsonPrimitive("object"),
							properties = mapOf(
								"road_report_pdf_content" to SchemaProperty(
									type = JsonPrimitive("string"),
									properties = null,
									required = null
								),
								"expected_output" to json.decodeFromJsonElement(ROAD_REPORT_OUTPUT_JSON_SCHEMA),
							),
							required = listOf("road_report_pdf_content", "expected_output")
						)
					),
					required = listOf("item")
				)
			),
			testingCriteria = listOf(
				ChatGptEvalsApiRequest.PythonGrader(
					name = "Road Report Extraction Matcher",
					source = pythonScriptGraderProvider.provide(properties.pythonGraderSrc),
					passThreshold = 0.8F
				)
			),
			metadata = Metadata(
				description = "Road report extraction eval for report"
			)
		)

		val response: ChatGptEval = chatGptHttpClient.post {
			url { path("v1/evals") }
			contentType(Json)
			setBody(evalRequest)
		}.body()
		return response.id
	}
}
