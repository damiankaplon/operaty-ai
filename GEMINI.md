## Unit tests

For unit tests use `kotlin.test` annotations. If there are no suitable annotations there then use `org.junit`.
If you find it often necessary to create an object of a certain class for testing purposes, please introduce a stub object taking care of building these objects.
Here is an example of such a stub building object:

```kotlin
object RoadReportStub {

	data class RoadReportBuilder(
		var id: UUID = UUID.randomUUID(),
		var versions: Set<RoadReport.Version> = setOf(RoadReportVersionStub {}),
	)

	operator fun invoke(adjust: RoadReportBuilder.() -> Unit): RoadReport {
		val builder = RoadReportBuilder()
		builder.adjust()
		return RoadReport(
			id = builder.id,
			versions = builder.versions
		)
	}
}

object RoadReportVersionStub {

	data class RoadReportVersionBuilder(
		var version: Int = 0,
		var reportNumber: Int = 2025,
		var area: String? = null,
		var roadNumber: String? = null,
		var from: String? = null,
		var to: String? = null,
		var detailed: String? = null,
		var task: String? = null,
		var report: String? = null,
		var measurementDate: String? = null,
		var reportDate: String? = null,
		var length: String? = null,
		var loweredCurb: String? = null,
		var rim: String? = null,
		var inOut: String? = null,
		var flat: String? = null,
		var pa: String? = null,
		var slope: String? = null,
		var ditch: String? = null,
		var demolition: String? = null,
		var surface: String? = null,
		var volume: String? = null,
		var inner: String? = null,
		var odh: String? = null,
		var dig: Float? = null,
		var infill: Float? = null,
		var bank: Float? = null,
		var excavation: Float? = null,
	)

	operator fun invoke(adjust: RoadReportVersionBuilder.() -> Unit): RoadReport.Version {
		val builder = RoadReportVersionBuilder().apply(adjust)
		return with(builder) {
			RoadReport.Version(
				version,
				RoadReport.Version.Content(
					reportNumber,
					area,
					roadNumber,
					from,
					to,
					detailed,
					task,
					report,
					measurementDate,
					reportDate,
					length,
					loweredCurb,
					rim,
					inOut,
					flat,
					pa,
					slope,
					ditch,
					demolition,
					surface,
					volume,
					inner,
					odh,
					dig,
					infill,
					bank,
					excavation
				)
			)
		}
	}
}
```

Repositories are not mocked. They have simple in memory implementations for test purposes e.g.:

```kotlin
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
```

All tests should have // given, // when, // then or // when and then sections.
All test classes responsible for running tests should be internal. On the other hand, all test helper classes should be simple public.
