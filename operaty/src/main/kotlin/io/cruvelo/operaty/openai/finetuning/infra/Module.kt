package io.cruvelo.operaty.openai.finetuning.infra

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.openai.files.OpenAiFileUploader
import io.cruvelo.operaty.openai.finetuning.FineTunerSourceProvider
import io.cruvelo.operaty.openai.finetuning.FineTunerSourceRoadReportProvider
import io.cruvelo.operaty.openai.http.ChatGptHttpClient
import io.cruvelo.operaty.report.road.ExposedRoadReportPdfContentRepository
import io.cruvelo.operaty.report.road.ExposedRoadReportRepository
import jakarta.inject.Singleton

@Module
class Module {

	@Provides
	@Singleton
	fun provideFineTunerSourceProvider(): FineTunerSourceProvider =
		FineTunerSourceRoadReportProvider(
			ExposedFineTunedModelRepository,
			ExposedRoadReportRepository
		)

	@Singleton
	@Provides
	fun provideOpenAiFineTuningHttp(chatGptHttpClient: ChatGptHttpClient): OpenAiFineTuningHttp =
		OpenAiFineTuningHttp(chatGptHttpClient)

	@Provides
	@Singleton
	fun provideRoadReportFineTuner(
		fineTunerSourceProvider: FineTunerSourceProvider,
		openAiFileUploader: OpenAiFileUploader,
		openAiFineTuningHttp: OpenAiFineTuningHttp,
	): RoadReportFineTuner = RoadReportFineTuner(
		fineTunerSourceProvider,
		ExposedRoadReportRepository,
		ExposedRoadReportPdfContentRepository,
		JsonlFineTuneEncoderKotlinx,
		JsonlFineTuneEncoderKotlinx,
		openAiFileUploader,
		openAiFineTuningHttp,
		ExposedFineTunedModelJsonlFileReferenceRepository,
		ExposedFineTunedModelRepository,
	)
}
