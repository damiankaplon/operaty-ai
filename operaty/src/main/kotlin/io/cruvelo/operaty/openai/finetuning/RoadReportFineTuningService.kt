package io.cruvelo.operaty.openai.finetuning

import io.cruvelo.operaty.report.road.RoadReport
import io.cruvelo.operaty.report.road.RoadReportRepository
import io.github.oshai.kotlinlogging.KotlinLogging

private val LOGGER = KotlinLogging.logger {}

class RoadReportFineTuningService(
	private val fineTunedModelRepository: FineTunedModelRepository,
	private val roadReportRepository: RoadReportRepository,
	private val fineTuner: FineTuner
) {

	fun fineTune() {
		val lastModifiedReports = roadReportRepository.findLastModified(50)
		if (lastModifiedReports.size <= 10) {
			LOGGER.debug { "Skipping fine-tuning. There are only ${lastModifiedReports.size} reports. At least 10 are required for fine tuning." }
			return
		}
		val mostAccurateModel = fineTunedModelRepository.findMostAccurate()
		val lastModifiedReportVersions: Set<FineTunedModel.Source> =
			lastModifiedReports.associateWith { report: RoadReport -> report.versions.maxBy(RoadReport.Version::version) }
				.map { (report: RoadReport, version: RoadReport.Version) -> FineTunedModel.Source(report.id, version.version) }
				.toSet()
		val sharedSources = lastModifiedReportVersions.intersect(mostAccurateModel?.source ?: emptySet())
		val newSourcesCount = lastModifiedReportVersions.size - sharedSources.size
		if (newSourcesCount <= 5) {
			LOGGER.debug { "Skipping fine-tuning. The most accurate model $mostAccurateModel is already sourced with similar set of data" }
			return
		}
		fineTuner.tune(sharedSources)
	}
}

fun interface FineTuner {

	fun tune(source: Set<FineTunedModel.Source>): FineTunedModel
}
