package io.cruvelo.operaty.openai.finetuning

interface FineTunedModelRepository {

	fun save(model: FineTunedModel): FineTunedModel

	fun findMostAccurate(): FineTunedModel?
}