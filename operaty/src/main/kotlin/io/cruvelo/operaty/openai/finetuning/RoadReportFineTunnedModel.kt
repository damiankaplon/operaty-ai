package io.cruvelo.operaty.openai.finetuning

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.jsonb

interface FineTunedModelRepository {

	fun save(model: FineTunedModel): FineTunedModel

	fun findMostAccurate(): FineTunedModel?
}

object ExposedFineTunedModelRepository : FineTunedModelRepository {

	override fun save(model: FineTunedModel): FineTunedModel {
		TODO("Not yet implemented")
	}

	override fun findMostAccurate(): FineTunedModel? {
		return FineTunedModelTabel.select(
			FineTunedModelTabel.id,
			FineTunedModelTabel.providerId,
			FineTunedModelTabel.sources,
			FineTunedModelTabel.accuracy,
			FineTunedModelTabel.createdOn
		).orderBy(FineTunedModelTabel.accuracy, SortOrder.DESC)
			.limit(1)
			.singleOrNull()
			?.let { row ->
				FineTunedModel(
					id = row[FineTunedModelTabel.id],
					providerId = row[FineTunedModelTabel.providerId],
					source = row[FineTunedModelTabel.sources],
				).apply { accuracy = row[FineTunedModelTabel.accuracy] }
			}
	}
}

private object FineTunedModelTabel : Table("report_road_fine_tuned_model") {

	val id = uuid("id")
	val providerId = text("provider_id")
	val sources = jsonb<Set<FineTunedModel.Source>>("sources", Json {})
	var accuracy = float("accuracy").nullable()
	val createdOn = timestamp("created_on")

	override val primaryKey = PrimaryKey(id)
}
