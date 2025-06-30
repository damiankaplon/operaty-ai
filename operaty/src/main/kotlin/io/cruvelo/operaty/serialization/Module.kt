package io.cruvelo.operaty.serialization

import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json

@Module
class Module {

	@Provides
	@Singleton
	fun provideJson(): Json = Json {
		ignoreUnknownKeys = true
		prettyPrint = true
	}
}
