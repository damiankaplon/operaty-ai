package io.cruvelo.operaty.openai.files

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import jakarta.inject.Singleton

@Module
class Module {

	@Singleton
	@Provides
	fun provideOpenAiFileUploader(
		chatGptHttpClient: ChatGptHttpClient,
	): OpenAiFileUploader = OpenAiFileUploader(chatGptHttpClient.client)
}
