package io.cruvelo.operaty.openai.evals

import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.cruvelo.operaty.report.road.RoadReport
import io.cruvelo.operaty.report.road.RoadReportPdfContentRepository
import io.cruvelo.operaty.report.road.RoadReportRepository
import io.ktor.client.request.delete
import io.ktor.http.path
import kotlinx.serialization.json.Json

class RoadReportEvalRunCreator(
	private val json: Json,
	chatGptHttpClient: ChatGptHttpClient,
	private val roadReportRepository: RoadReportRepository,
	private val roadReportPdfContentRepository: RoadReportPdfContentRepository,
	private val roadReportJsonlFileRepository: RoadReportJsonlFileRepository,
	private val roadReportEvalProvider: RoadReportEvalProvider,
) {

	private val chatGptHttpClient = chatGptHttpClient.client

	suspend fun createRun(roadReport: RoadReport) {
		val fileReference = roadReportJsonlFileRepository.findByRoadReportId(roadReport.id)
		if (fileReference != null) {
			chatGptHttpClient.delete { url { path("v1/files/$fileReference") } }
		}

	}
}
