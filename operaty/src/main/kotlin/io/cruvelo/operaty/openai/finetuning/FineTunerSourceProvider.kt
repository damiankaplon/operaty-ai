package io.cruvelo.operaty.openai.finetuning

interface FineTunerSourceProvider {

	fun provide(): Set<FineTunedModel.Source>
}
