package io.cruvelo.operaty.openai.finetuning.infra

import io.cruvelo.operaty.openai.finetuning.FineTunedModel
import io.cruvelo.operaty.openai.finetuning.FineTunedModelRepository
import io.cruvelo.persistance.ObjectNotFoundException
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

object ExposedFineTunedModelRepository : FineTunedModelRepository {

	override fun save(model: FineTunedModel): FineTunedModel {
		FineTunedModelTabel.upsert { row -> row.from(model) }
		return findByIdOrThrow(model.id)
	}

	private fun findById(id: UUID): FineTunedModel? {
		return FineTunedModelTabel.select(FineTunedModelTabel.columns)
			.where { FineTunedModelTabel.id eq id }
			.singleOrNull()?.toFineTunedModel()
	}

	private fun findByIdOrThrow(id: UUID): FineTunedModel =
		findById(id) ?: throw ObjectNotFoundException("Fine-tuned model not found for ID $id")

	private fun <T : Any> InsertStatement<T>.from(model: FineTunedModel) {
		this[FineTunedModelTabel.id] = model.id
		this[FineTunedModelTabel.providerId] = model.providerId
		this[FineTunedModelTabel.sources] = model.source
		this[FineTunedModelTabel.accuracy] = model.accuracy
	}

	private fun ResultRow.toFineTunedModel(): FineTunedModel =
		FineTunedModel(
			id = this[FineTunedModelTabel.id],
			providerId = this[FineTunedModelTabel.providerId],
			source = this[FineTunedModelTabel.sources],
		).apply { accuracy = this@toFineTunedModel[FineTunedModelTabel.accuracy] }

	override fun findMostAccurate(): FineTunedModel? {
		return FineTunedModelTabel.select(FineTunedModelTabel.columns)
			.orderBy(FineTunedModelTabel.accuracy, SortOrder.DESC)
			.limit(1)
			.singleOrNull()?.toFineTunedModel()
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
