package io.cruvelo.operaty.openai.evals

import io.cruvelo.operaty.report.road.ContentStub
import io.cruvelo.operaty.report.road.RoadReport
import io.cruvelo.operaty.report.road.RoadReportInMemoryRepository
import io.cruvelo.operaty.report.road.RoadReportPdfContentInMemoryRepository
import io.cruvelo.operaty.report.road.RoadReportPdfContentRepository
import io.cruvelo.operaty.report.road.RoadReportRepository
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class JsonlFileEvalRunFactoryTest {

	private lateinit var factory: JsonlFileEvalRunFactory
	private lateinit var roadReportRepository: RoadReportRepository
	private lateinit var roadReportPdfContentRepository: RoadReportPdfContentRepository

	@BeforeTest
	fun setUp() {
		roadReportRepository = RoadReportInMemoryRepository()
		roadReportPdfContentRepository = RoadReportPdfContentInMemoryRepository()
		factory = DefaultJsonlFileEvalRunFactory(
			Json { ignoreUnknownKeys = true; prettyPrint = false },
			roadReportRepository,
			roadReportPdfContentRepository
		)
	}

	@Test
	fun `should return jsonl file`() {
		// given
		val roadReport = RoadReport(
			UUID.randomUUID(),
			ContentStub {
				reportNumber = 1
				roadNumber = "S11"
				reportDate = "2023-01-01"
				excavation = 1000f
				bank = 100f
			}
		)
		roadReportRepository.save(roadReport)
		val content = """
			report number 1
			road number S11
			report date 2023-01-01
			excavation 1000
			bank 100
		""".trimIndent()
		roadReportPdfContentRepository.save(roadReport.id, content)

		// when
		val result = factory.create(roadReport.id)

		// then
		val expectedResult = """
			{
				"road_report_pdf_content":"reportnumber1\nroadnumberS11\nreportdate2023-01-01\nexcavation1000\nbank100",
				"expected_output":{
					"reportNumber":1,
					"area":null,
					"roadNumber":"S11",
					"from":null,
					"to":null,
					"detailed":null,
					"task":null,
					"report":null,
					"measurementDate":null,
					"reportDate":"2023-01-01",
					"length":null,
					"loweredCurb":null,
					"rim":null,
					"inOut":null,
					"flat":null,
					"pa":null,
					"slope":null,
					"ditch":null,
					"demolition":null,
					"surface":null,
					"volume":null,
					"inner":null,
					"odh":null,
					"dig":null,
					"infill":null,
					"bank":100.0,
					"excavation":1000.0
				}
			}
		""".trimIndent().replace("\n", "").replace(" ", "").replace("	", "")
		assertThat(result.replace(" ", "")).isEqualTo(expectedResult)
	}
}
