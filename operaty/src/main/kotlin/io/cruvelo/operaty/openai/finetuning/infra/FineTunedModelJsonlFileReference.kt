package io.cruvelo.operaty.openai.finetuning.infra

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert

interface FineTunedModelJsonlFileReferenceRepository {

	fun save(fineTunedModelId: String, fileReference: String)
}

object ExposedFineTunedModelJsonlFileReferenceRepository : FineTunedModelJsonlFileReferenceRepository {

	override fun save(fineTunedModelId: String, fileReference: String) {
		FineTunedModelJsonlFileReferenceTabel.insert {
			it[this.fineTunedModelId] = fineTunedModelId
			it[this.fileReference] = fileReference
		}
	}
}

private object FineTunedModelJsonlFileReferenceTabel : Table("report_road_fine_tuned_model_jsonl_file_reference") {

	val fineTunedModelId = text("fine_tuned_model_id")
	val fileReference = text("file_reference")

	override val primaryKey = PrimaryKey(fineTunedModelId, fileReference)
}
