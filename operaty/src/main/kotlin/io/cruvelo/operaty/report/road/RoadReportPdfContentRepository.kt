package io.cruvelo.operaty.report.road

import io.cruvelo.persistance.ObjectNotFoundException
import org.jetbrains.exposed.sql.insert
import java.util.UUID

interface RoadReportPdfContentRepository {

	fun save(roadReportId: UUID, content: String)
	fun findContentByReportId(reportId: UUID): String?
	fun findContentByIdOrThrow(reportId: UUID): String =
		findContentByReportId(reportId)
			?: throw ObjectNotFoundException("Road report PDF content not found for report ID $reportId")
}

object ExposedRoadReportPdfContentRepository : RoadReportPdfContentRepository {

	override fun save(roadReportId: UUID, content: String) {
		RoadReportPdfContentTable.insert {
			it[reportId] = roadReportId
			it[pdfContent] = content
		}
	}

	override fun findContentByReportId(reportId: UUID): String? {
		return RoadReportPdfContentTable.select(RoadReportPdfContentTable.pdfContent)
			.where { RoadReportPdfContentTable.reportId eq reportId }
			.singleOrNull()
			?.let { it[RoadReportPdfContentTable.pdfContent] }
	}
}
