package io.cruvelo.operaty.openai.files

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.InputStream

class OpenAiFileUploader(
	private val httpClient: HttpClient,
) {

	suspend fun upload(uploadFile: UploadFile): FileId {
		val response = httpClient.submitFormWithBinaryData(
			url = "https://api.openai.com/v1/files",
			formData = formData {
				append("purpose", "fine-tune")
				append(
					"file", uploadFile.file.readBytes(),
					Headers.build {
						append(HttpHeaders.ContentType, "application/json")
						append(HttpHeaders.ContentDisposition, "filename=${uploadFile.filename}")
					}
				)
			}
		)

		val jsonResponse = Json.parseToJsonElement(response.bodyAsText())
		val fileId = jsonResponse.jsonObject["id"]?.jsonPrimitive?.content ?: error("No id was returned on $uploadFile")
		return FileId(fileId)
	}

	data class UploadFile(
		val file: InputStream,
		val purpose: String = "fine-tune",
		val filename: String = "data.jsonl",
	)

	@JvmInline
	value class FileId(val value: String)
}
