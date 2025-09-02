package io.cruvelo.operaty.openai.evals

import io.cruvelo.operaty.report.road.RoadReportTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

interface RoadReportJsonlFileRepository {

	fun save(roadReportId: UUID, fileReference: String)
	fun findByRoadReportId(roadReportId: UUID): String?
}

object ExposedRoadReportJsonlFileRepository : RoadReportJsonlFileRepository {

	override fun save(roadReportId: UUID, fileReference: String) {
		RoadReportEvalRunFileTable.upsert {
			it[RoadReportEvalRunFileTable.reportId] = roadReportId
			it[RoadReportEvalRunFileTable.openAiFileStorageReference] = fileReference
		}
	}

	override fun findByRoadReportId(roadReportId: UUID): String? {
		return RoadReportEvalRunFileTable.select(RoadReportEvalRunFileTable.openAiFileStorageReference)
			.where { RoadReportEvalRunFileTable.reportId eq roadReportId }
			.singleOrNull()
			?.get(RoadReportEvalRunFileTable.openAiFileStorageReference)
	}
}

object RoadReportEvalRunFileTable : Table("report_road_version") {

	val reportId = uuid("report_id").references(RoadReportTable.id)
	val openAiFileStorageReference = text("open_ai_file_storage_reference")

	override val primaryKey = PrimaryKey(reportId)
}