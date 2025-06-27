package io.cruvelo.operaty

import dagger.BindsInstance
import dagger.Component
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.cruvelo.operaty.openai.http.HttpModule
import io.ktor.server.config.ApplicationConfig
import jakarta.inject.Singleton

@Singleton
@Component(
	modules = [HttpModule::class]
)
interface AppComponent {

	fun chatGptHttpClient(): ChatGptHttpClient

	@Component.Builder
	interface Builder {

		@BindsInstance
		fun applicationConfig(config: ApplicationConfig): Builder
		fun build(): AppComponent
	}
}
