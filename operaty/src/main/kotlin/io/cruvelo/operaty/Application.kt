package io.cruvelo.operaty

import FileUploadResponse
import io.cruvelo.operaty.openai.Schemas
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest.Input
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest.Input.Content
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest.Prompt
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest.Text
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiRequest.Text.Format
import io.cruvelo.operaty.openai.http.ChatGptResponsesApiResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import java.util.UUID

private val LOGGER = KotlinLogging.logger {}

fun main(args: Array<String>) {
	EngineMain.main(args)
}

fun Application.module() {
	install(ContentNegotiation) { json() }
	val appComponent = DaggerAppComponent.builder()
		.applicationConfig(environment.config)
		.build()
	val chatGptHttpClient: HttpClient = appComponent.chatGptHttpClient().client
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			LOGGER.error(throwable = cause) { "Application internal error:" }
			call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
		}
	}
	routing {
		route("api") {
			route("reports") {
				post("road") {
					val multipart = call.receiveMultipart()
					var fileBytes: ByteArray = byteArrayOf()

					multipart.forEachPart { part: PartData ->
						when (part) {
							is PartData.FileItem -> {
								val channel: ByteReadChannel = part.provider()
								fileBytes = fileBytes.plus(channel.readRemaining().readByteArray())
							}
							else -> {}
						}
						part.dispose()
					}

					val fileResponse = chatGptHttpClient.post {
						url { path("v1/files") }
						setBody(
							MultiPartFormDataContent(
								formData {
									append(
										key = "purpose",
										value = "user_data"
									)
									append(
										key = "file",
										value = fileBytes,
										headers = headers {
											append(HttpHeaders.ContentDisposition, "filename=${UUID.randomUUID()}.pdf")
										}
									)
								}
							)
						)
					}
					val fileId: String = fileResponse.body<FileUploadResponse>()
						.let(FileUploadResponse::id)

					val response: ChatGptResponsesApiResponse = chatGptHttpClient.post {
						url { path("v1/responses") }
						contentType(Json)
						setBody(
							ChatGptResponsesApiRequest(
								model = "gpt-4.1",
								prompt = Prompt(
									id = "pmpt_685c304658a081978b7633929d9785f702284f6f4e91ded8",
									version = "7"
								),
								input = setOf(
									Input(
										role = "user",
										content = setOf(Content("input_file", fileId))
									)
								),
								text = Text(
									format = Format(
										name = "text",
										type = "json_schema",
										schema = Schemas.ROAD_REPORT_OUTPUT_JSON_SCHEMA,
										description = "Extracts the road report data from the PDF file"
									)
								)
							)
						)
					}.body<ChatGptResponsesApiResponse>()
					val roadReport: RoadReport = appComponent.json().decodeFromString<RoadReport>(response.output.first().content.first().text)
					call.respond(roadReport)
				}
			}
		}
	}
}
