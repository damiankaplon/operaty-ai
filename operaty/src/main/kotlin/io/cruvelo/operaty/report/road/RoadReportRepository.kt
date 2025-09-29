package io.cruvelo.operaty.report.road

import io.cruvelo.persistance.ObjectNotFoundException
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

interface RoadReportRepository {

	fun save(roadReport: RoadReport)

	fun findByIdOrThrow(id: UUID): RoadReport =
		findById(id) ?: throw ObjectNotFoundException("Road report not found")

	fun findById(id: UUID): RoadReport?

	fun findAll(): Set<RoadReport>

	fun findLastModified(size: Int): Set<RoadReport>
}

object ExposedRoadReportRepository : RoadReportRepository {

	override fun save(roadReport: RoadReport) {
		RoadReportTable.upsert { it.from(roadReport) }
		roadReport.versions.forEach { version ->
			RoadReportVersionTable.upsert {
				it[reportId] = roadReport.id
				it.from(version)
			}
		}
	}

	override fun findById(id: UUID): RoadReport? {
		return RoadReportTable.select(RoadReportTable.columns).where { RoadReportTable.id eq id }
			.singleOrNull()
			?.let { reportRow: ResultRow ->
				val versions: Set<RoadReport.Version> = RoadReportVersionTable.select(RoadReportVersionTable.columns).where { RoadReportVersionTable.reportId eq id }
					.map { versionRow: ResultRow -> versionRow.toRoadReportVersion() }
					.toSet()
				return@let RoadReport(
					id = reportRow[RoadReportTable.id],
					versions = versions.toSet()
				)
			}
	}

	override fun findAll(): Set<RoadReport> {
		return RoadReportTable.select(RoadReportTable.columns).map { reportRow: ResultRow ->
			val versions: Set<RoadReport.Version> = RoadReportVersionTable.select(RoadReportVersionTable.columns).where { RoadReportVersionTable.reportId eq reportRow[RoadReportTable.id] }
				.map { versionRow: ResultRow -> versionRow.toRoadReportVersion() }
				.toSet()
			return@map RoadReport(
				id = reportRow[RoadReportTable.id],
				versions = versions.toSet()
			)
		}.toSet()
	}

	override fun findLastModified(size: Int): Set<RoadReport> {
		return RoadReportTable.join(
			otherTable = RoadReportVersionTable,
			joinType = JoinType.INNER,
			onColumn = RoadReportTable.id,
			otherColumn = RoadReportVersionTable.reportId,
			lateral = false,
			additionalConstraint = null
		).select(RoadReportTable.columns + RoadReportVersionTable.columns)
			.orderBy(RoadReportVersionTable.version, SortOrder.DESC)
			.limit(size)
			.groupBy { it[RoadReportTable.id] }
			.mapTo(mutableSetOf()) { (roadReportId: UUID, rows: List<ResultRow>) ->
				return@mapTo RoadReport(
					id = roadReportId,
					versions = rows.map { it.toRoadReportVersion() }.toSet()
				)
			}
	}

	private fun ResultRow.toRoadReportVersion(): RoadReport.Version {
		return RoadReport.Version(
			version = this[RoadReportVersionTable.version],
			content = RoadReport.Version.Content(
				reportNumber = this[RoadReportVersionTable.reportNumber],
				area = this[RoadReportVersionTable.area],
				roadNumber = this[RoadReportVersionTable.roadNumber],
				from = this[RoadReportVersionTable.from],
				to = this[RoadReportVersionTable.to],
				detailed = this[RoadReportVersionTable.detailed],
				task = this[RoadReportVersionTable.task],
				report = this[RoadReportVersionTable.report],
				measurementDate = this[RoadReportVersionTable.measurementDate],
				reportDate = this[RoadReportVersionTable.reportDate],
				length = this[RoadReportVersionTable.length],
				loweredCurb = this[RoadReportVersionTable.loweredCurb],
				rim = this[RoadReportVersionTable.rim],
				inOut = this[RoadReportVersionTable.inOut],
				flat = this[RoadReportVersionTable.flat],
				pa = this[RoadReportVersionTable.pa],
				slope = this[RoadReportVersionTable.slope],
				ditch = this[RoadReportVersionTable.ditch],
				demolition = this[RoadReportVersionTable.demolition],
				surface = this[RoadReportVersionTable.surface],
				volume = this[RoadReportVersionTable.volume],
				inner = this[RoadReportVersionTable.inner],
				odh = this[RoadReportVersionTable.odh],
				dig = this[RoadReportVersionTable.dig],
				infill = this[RoadReportVersionTable.infill],
				bank = this[RoadReportVersionTable.bank],
				excavation = this[RoadReportVersionTable.excavation],
			)
		)
	}
}