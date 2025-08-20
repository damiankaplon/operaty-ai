package io.cruvelo.operaty.report.road

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.db.TransactionalRunner
import io.cruvelo.operaty.openai.EvalsUpdater
import io.cruvelo.operaty.openai.ResourcesPythonGraderProvider
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json

@Module
class Module {

	@Provides
	@Singleton
	fun evalsUpdater(
		properties: EvalsUpdater.Properties,
		chatGptHttpClient: ChatGptHttpClient,
		json: Json,
	): EvalsUpdater {
		return EvalsUpdater(
			properties,
			ResourcesPythonGraderProvider,
			chatGptHttpClient.client,
			json
		)
	}

	@Provides
	@Singleton
	fun roadReportController(
		transactionalRunner: TransactionalRunner,
		chatGtpHttpClient: ChatGptHttpClient,
		evalsUpdater: EvalsUpdater,
		json: Json,
	) =
		RoadReportController(
			ExposedRoadReportRepository,
			ExposedRoadReportPdfContentRepository,
			transactionalRunner,
			chatGtpHttpClient.client,
			evalsUpdater,
			json
		)
}
