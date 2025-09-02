package io.cruvelo.operaty.report.road

import java.util.UUID

class RoadReportPdfContentInMemoryRepository : RoadReportPdfContentRepository {

	private val storage = mutableMapOf<UUID, String>()

	override fun save(roadReportId: UUID, content: String) {
		storage[roadReportId] = content
	}

	override fun findContentByReportId(reportId: UUID): String? {
		return storage[reportId]
	}
}
