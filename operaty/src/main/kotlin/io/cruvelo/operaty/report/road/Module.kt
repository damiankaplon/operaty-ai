package io.cruvelo.operaty.report.road

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.db.TransactionalRunner
import jakarta.inject.Singleton

@Module
class Module {

	@Provides
	@Singleton
	fun roadReportController(transactionalRunner: TransactionalRunner) = RoadReportController(transactionalRunner)
}
