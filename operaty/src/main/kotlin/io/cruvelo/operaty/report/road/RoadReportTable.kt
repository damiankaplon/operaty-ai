package io.cruvelo.operaty.report.road

import io.cruvelo.operaty.report.road.RoadReportTable.id
import io.cruvelo.operaty.report.road.RoadReportVersionTable.area
import io.cruvelo.operaty.report.road.RoadReportVersionTable.bank
import io.cruvelo.operaty.report.road.RoadReportVersionTable.demolition
import io.cruvelo.operaty.report.road.RoadReportVersionTable.detailed
import io.cruvelo.operaty.report.road.RoadReportVersionTable.dig
import io.cruvelo.operaty.report.road.RoadReportVersionTable.ditch
import io.cruvelo.operaty.report.road.RoadReportVersionTable.excavation
import io.cruvelo.operaty.report.road.RoadReportVersionTable.flat
import io.cruvelo.operaty.report.road.RoadReportVersionTable.from
import io.cruvelo.operaty.report.road.RoadReportVersionTable.inOut
import io.cruvelo.operaty.report.road.RoadReportVersionTable.infill
import io.cruvelo.operaty.report.road.RoadReportVersionTable.inner
import io.cruvelo.operaty.report.road.RoadReportVersionTable.length
import io.cruvelo.operaty.report.road.RoadReportVersionTable.loweredCurb
import io.cruvelo.operaty.report.road.RoadReportVersionTable.measurementDate
import io.cruvelo.operaty.report.road.RoadReportVersionTable.odh
import io.cruvelo.operaty.report.road.RoadReportVersionTable.pa
import io.cruvelo.operaty.report.road.RoadReportVersionTable.report
import io.cruvelo.operaty.report.road.RoadReportVersionTable.reportDate
import io.cruvelo.operaty.report.road.RoadReportVersionTable.reportNumber
import io.cruvelo.operaty.report.road.RoadReportVersionTable.rim
import io.cruvelo.operaty.report.road.RoadReportVersionTable.roadNumber
import io.cruvelo.operaty.report.road.RoadReportVersionTable.slope
import io.cruvelo.operaty.report.road.RoadReportVersionTable.surface
import io.cruvelo.operaty.report.road.RoadReportVersionTable.task
import io.cruvelo.operaty.report.road.RoadReportVersionTable.to
import io.cruvelo.operaty.report.road.RoadReportVersionTable.version
import io.cruvelo.operaty.report.road.RoadReportVersionTable.volume
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement

object RoadReportTable : Table("report_road") {

	val id = uuid("id")
	override val primaryKey = PrimaryKey(id)
}

object RoadReportPdfContentTable : Table("report_road_pdf_content") {

	val reportId = uuid("report_id").references(id)
	val pdfContent = text("pdf_content")

	override val primaryKey = PrimaryKey(reportId)
}

object RoadReportVersionTable : Table("report_road_version") {

	val reportId = uuid("report_id").references(id)
	val version = integer("version")
	val reportNumber = integer("report_number")
	val area = text("area").nullable()
	val roadNumber = text("road_number").nullable()
	val from = text("from").nullable()
	val to = text("to").nullable()
	val detailed = text("detailed").nullable()
	val task = text("task").nullable()
	val report = text("report").nullable()
	val measurementDate = text("measurement_date").nullable()
	val reportDate = text("report_date").nullable()
	val length = text("length").nullable()
	val loweredCurb = text("lowered_curb").nullable()
	val rim = text("rim").nullable()
	val inOut = text("in_out").nullable()
	val flat = text("flat").nullable()
	val pa = text("pa").nullable()
	val slope = text("slope").nullable()
	val ditch = text("ditch").nullable()
	val demolition = text("demolition").nullable()
	val surface = text("surface").nullable()
	val volume = text("volume").nullable()
	val inner = text("inner").nullable()
	val odh = text("odh").nullable()
	val dig = float("dig").nullable()
	val infill = float("infill").nullable()
	val bank = float("bank").nullable()
	val excavation = float("excavation").nullable()

	override val primaryKey = PrimaryKey(reportId, version)
}

fun <T : Any> InsertStatement<T>.from(roadReport: RoadReport) {
	this[id] = roadReport.id
}

fun <T : Any> InsertStatement<T>.from(roadReportVersion: RoadReport.Version) {
	this[version] = roadReportVersion.version
	this[reportNumber] = roadReportVersion.content.reportNumber
	this[area] = roadReportVersion.content.area
	this[roadNumber] = roadReportVersion.content.roadNumber
	this[from] = roadReportVersion.content.from
	this[to] = roadReportVersion.content.to
	this[detailed] = roadReportVersion.content.detailed
	this[task] = roadReportVersion.content.task
	this[report] = roadReportVersion.content.report
	this[measurementDate] = roadReportVersion.content.measurementDate
	this[reportDate] = roadReportVersion.content.reportDate
	this[length] = roadReportVersion.content.length
	this[loweredCurb] = roadReportVersion.content.loweredCurb
	this[rim] = roadReportVersion.content.rim
	this[inOut] = roadReportVersion.content.inOut
	this[flat] = roadReportVersion.content.flat
	this[pa] = roadReportVersion.content.pa
	this[slope] = roadReportVersion.content.slope
	this[ditch] = roadReportVersion.content.ditch
	this[demolition] = roadReportVersion.content.demolition
	this[surface] = roadReportVersion.content.surface
	this[volume] = roadReportVersion.content.volume
	this[inner] = roadReportVersion.content.inner
	this[odh] = roadReportVersion.content.odh
	this[dig] = roadReportVersion.content.dig
	this[infill] = roadReportVersion.content.infill
	this[bank] = roadReportVersion.content.bank
	this[excavation] = roadReportVersion.content.excavation
}
