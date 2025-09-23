package io.cruvelo.operaty.report.road

import java.util.UUID

class RoadReportInMemoryRepository : RoadReportRepository {

	private val reports = mutableSetOf<RoadReport>()

	override fun save(roadReport: RoadReport) {
		reports.removeIf { it.id == roadReport.id }
		reports.add(roadReport)
	}

	override fun findById(id: UUID): RoadReport? {
		return reports.find { it.id == id }
	}

	override fun findAll(): Set<RoadReport> {
		return reports.toSet()
	}

	override fun findLastModified(size: Int): Set<RoadReport> {
		return reports.sortedByDescending { it.versions.last().version }.take(size).toSet()
	}
}
