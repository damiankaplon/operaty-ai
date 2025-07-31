package io.cruvelo.operaty.report.road

import java.util.UUID

class RoadReport(val id: UUID) {

	constructor(id: UUID, initialVersion: Version.Content) : this(id) {
		versions = setOf(Version(0, initialVersion))
	}

	constructor(id: UUID, versions: Set<Version>) : this(id) {
		this.versions = versions
	}

	var versions: Set<Version> = emptySet(); private set

	fun newVersion(version: Version.Content) {
		versions += Version(versions.size, version)
	}

	data class Version(
		val version: Int,
		val content: Content,
	) {

		data class Content(
			val reportNumber: Int,
			val area: String?,
			val roadNumber: String?,
			val from: String?,
			val to: String?,
			val detailed: String?,
			val task: String?,
			val report: String?,
			val measurementDate: String?,
			val reportDate: String?,
			val length: String?,
			val loweredCurb: String?,
			val rim: String?,
			val inOut: String?,
			val flat: String?,
			val pa: String?,
			val slope: String?,
			val ditch: String?,
			val demolition: String?,
			val surface: String?,
			val volume: String?,
			val inner: String?,
			val odh: String?,
			val dig: Float?,
			val infill: Float?,
			val bank: Float?,
			val excavation: Float?,
		)
	}
}