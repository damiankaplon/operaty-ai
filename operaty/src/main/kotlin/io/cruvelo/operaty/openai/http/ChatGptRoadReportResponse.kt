package io.cruvelo.operaty.openai.http

import kotlinx.serialization.Serializable

@Serializable
data class ChatGptRoadReportResponse(
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