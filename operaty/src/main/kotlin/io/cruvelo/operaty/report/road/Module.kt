package io.cruvelo.operaty.report.road

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.db.TransactionalRunner
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json

@Module
class Module {

	@Provides
	@Singleton
	fun roadReportController(
		transactionalRunner: TransactionalRunner,
		chatGtpHttpClient: ChatGptHttpClient,
		json: Json,
	) =
		RoadReportController(
			ExposedRoadReportRepository,
			ExposedRoadReportPdfContentRepository,
			transactionalRunner,
			chatGtpHttpClient,
			json
		)
}
