package io.cruvelo.operaty.report.road

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.db.TransactionalRunner
import io.cruvelo.operaty.openai.evals.DefaultJsonlFileEvalRunFactory
import io.cruvelo.operaty.openai.evals.JsonlFileEvalRunFactory
import io.cruvelo.operaty.openai.evals.ResourcesPythonScriptGraderProvider
import io.cruvelo.operaty.openai.evals.RoadReportEvalProvider
import io.cruvelo.operaty.openai.finetuning.infra.RoadReportFineTuner
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json

@Module
class Module {

	@Provides
	@Singleton
	fun evalsUpdater(
		properties: RoadReportEvalProvider.Properties,
		chatGptHttpClient: ChatGptHttpClient,
		json: Json,
	): RoadReportEvalProvider {
		return RoadReportEvalProvider(
			properties,
			ResourcesPythonScriptGraderProvider,
			chatGptHttpClient.client,
			json
		)
	}

	@Provides
	@Singleton
	fun jsonlFileEvalRunFactory(
		roadReportRepository: RoadReportRepository,
		roadReportPdfContentRepository: RoadReportPdfContentRepository,
	): JsonlFileEvalRunFactory = DefaultJsonlFileEvalRunFactory(
		Json { ignoreUnknownKeys = true; prettyPrint = false },
		roadReportRepository,
		roadReportPdfContentRepository,
	)

	@Provides
	@Singleton
	fun roadReportController(
		transactionalRunner: TransactionalRunner,
		chatGtpHttpClient: ChatGptHttpClient,
		json: Json,
		roadReportFineTuner: RoadReportFineTuner,
	) =
		RoadReportController(
			ExposedRoadReportRepository,
			ExposedRoadReportPdfContentRepository,
			transactionalRunner,
			chatGtpHttpClient,
			json,
			roadReportFineTuner
		)
}
