package io.cruvelo.operaty.openai.evals

import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.cruvelo.operaty.report.road.RoadReport
import io.cruvelo.operaty.report.road.RoadReportPdfContentRepository
import io.cruvelo.operaty.report.road.RoadReportRepository
import kotlinx.serialization.json.Json

class RoadReportEvalRunCreator(
	private val json: Json,
	chatGptHttpClient: ChatGptHttpClient,
	private val roadReportRepository: RoadReportRepository,
	private val roadReportPdfContentRepository: RoadReportPdfContentRepository,
	private val roadReportEvalProvider: RoadReportEvalProvider,
) {

	private val chatGptHttpClient = chatGptHttpClient.client

	suspend fun createRun(roadReport: RoadReport) {
	}
}
