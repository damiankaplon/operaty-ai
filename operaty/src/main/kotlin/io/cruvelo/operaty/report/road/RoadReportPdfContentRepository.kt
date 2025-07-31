package io.cruvelo.operaty.report.road

import org.jetbrains.exposed.sql.insert
import java.util.UUID

interface RoadReportPdfContentRepository {

	fun save(roadReportId: UUID, content: String)
}

object ExposedRoadReportPdfContentRepository : RoadReportPdfContentRepository {

	override fun save(roadReportId: UUID, content: String) {
		RoadReportPdfContentTable.insert {
			it[reportId] = roadReportId
			it[pdfContent] = content
		}
	}
}
