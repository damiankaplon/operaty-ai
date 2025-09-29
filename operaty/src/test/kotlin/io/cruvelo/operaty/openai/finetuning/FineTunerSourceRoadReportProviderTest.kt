package io.cruvelo.operaty.openai.finetuning

import io.cruvelo.operaty.report.road.RoadReportInMemoryRepository
import io.cruvelo.operaty.report.road.RoadReportRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class FineTunerSourceRoadReportProviderTest {

	private lateinit var fineTunedModelRepository: FineTunedModelRepository

	private lateinit var roadReportRepository: RoadReportRepository

	private lateinit var fineTunerSourceProvider: FineTunerSourceProvider

	@BeforeTest
	fun setUp() {
		fineTunedModelRepository = FineTunedModelInMemoryRepository()
		roadReportRepository = RoadReportInMemoryRepository()
		fineTunerSourceProvider = FineTunerSourceRoadReportProvider(fineTunedModelRepository, roadReportRepository)
	}

	@Test
	fun `should return empty set given there is less than 10 road reports to train model on`() {
		// given: 10 reports only
		repeat(10) { i -> roadReportRepository.save(RoadReportStub {}) }

		// when
		val result = fineTunerSourceProvider.provide()

		// then
		assertTrue(result.isEmpty(), "Should return empty set when <= 10 reports are available")
	}

	@Test
	fun `should return empty set given to little difference between last modified reports and most accurate model`() {
		// given: 16 reports, but only up to 5 new sources compared to most accurate model
		val reports = (0 until 16).map { i -> RoadReportStub {}.also { roadReportRepository.save(it) } }
		// lastModifiedReportVersions are pairs (id, 0) for all reports
		val lastModifiedSources: Set<FineTunedModel.Source> = reports.map { FineTunedModel.Source(it.id, 0) }.toSet()
		val sharedCount = lastModifiedSources.size - 4 // 12 shared, 4 new -> newSourcesCount = 4 <= 5
		val shared = lastModifiedSources.take(sharedCount).toSet()
		val mostAccurateModel = FineTunedModel(UUID.randomUUID(), providerId = "test-model", source = shared).apply { accuracy = 0.95f }
		fineTunedModelRepository.save(mostAccurateModel)

		// when
		val result = fineTunerSourceProvider.provide()

		// then
		assertTrue(result.isEmpty(), "Should return empty set when new sources count <= 5")
	}

	@Test
	fun `should return shared sources for fine tuning`() {
		// given: 16 reports and more than 5 new sources compared to most accurate model
		val reports = (0 until 16).map { i -> RoadReportStub {}.also { roadReportRepository.save(it) } }
		val lastModifiedSources: Set<FineTunedModel.Source> = reports.map { FineTunedModel.Source(it.id, 0) }.toSet()
		val shared = lastModifiedSources.take(10).toSet() // 6 new sources > 5
		val mostAccurateModel = FineTunedModel(UUID.randomUUID(), providerId = "test-model", source = shared).apply { accuracy = 0.90f }
		fineTunedModelRepository.save(mostAccurateModel)

		// when
		val result = fineTunerSourceProvider.provide()

		// then
		assertEquals(shared, result, "Should return the set of shared sources")
	}
}