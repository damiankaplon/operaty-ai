package io.cruvelo.operaty.openai.finetuning

fun interface FineTuner {

	suspend fun tune(source: Set<FineTunedModel.Source>): FineTunedModel
}
