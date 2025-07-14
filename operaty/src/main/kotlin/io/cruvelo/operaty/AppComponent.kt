package io.cruvelo.operaty

import dagger.BindsInstance
import dagger.Component
import io.cruvelo.operaty.db.TransactionalRunner
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.cruvelo.operaty.report.road.RoadReportController
import io.ktor.server.config.ApplicationConfig
import jakarta.inject.Singleton

@Singleton
@Component(
	modules = [
		io.cruvelo.operaty.openai.http.HttpModule::class,
		io.cruvelo.operaty.serialization.Module::class,
		io.cruvelo.operaty.db.dagger.Module::class,
		io.cruvelo.operaty.report.road.Module::class,
	]
)
interface AppComponent {

	fun chatGptHttpClient(): ChatGptHttpClient
	fun json(): kotlinx.serialization.json.Json
	fun transactionalRunner(): TransactionalRunner
	fun roadReportController(): RoadReportController

	@Component.Builder
	interface Builder {

		@BindsInstance
		fun applicationConfig(config: ApplicationConfig): Builder
		fun build(): AppComponent
	}
}
