package io.cruvelo.operaty.openai.http

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.openai.RoadReportEvalProvider
import io.ktor.server.config.ApplicationConfig
import jakarta.inject.Singleton

@Module
class Module {

	@Singleton
	@Provides
	fun provideEvalsProviderProperties(appConfig: ApplicationConfig): RoadReportEvalProvider.Properties =
		RoadReportEvalProvider.Properties(
			name = appConfig.property("app.chatGpt.evals.roadReport.name").getString(),
			pythonGraderSrc = appConfig.property("app.chatGpt.evals.roadReport.name.pythonGraderSrc").getString(),
		)
}
