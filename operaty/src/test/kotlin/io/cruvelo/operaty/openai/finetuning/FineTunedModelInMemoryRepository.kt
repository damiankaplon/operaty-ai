package io.cruvelo.operaty.openai.finetuning

internal class FineTunedModelInMemoryRepository : FineTunedModelRepository {

	private val models: MutableSet<FineTunedModel> = mutableSetOf()

	override fun save(model: FineTunedModel): FineTunedModel {
		models.add(model)
		return model
	}

	override fun findMostAccurate(): FineTunedModel? {
		return models.maxByOrNull { it.accuracy ?: 0.0f }
	}
}
