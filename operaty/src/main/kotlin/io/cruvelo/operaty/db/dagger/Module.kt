package io.cruvelo.operaty.db.dagger

import dagger.Module
import dagger.Provides
import io.cruvelo.operaty.db.ExposedTransactionalRunner
import io.cruvelo.operaty.db.HikariCPDataSourceProvider
import io.cruvelo.operaty.db.KtorEnvDatabaseConfigProvider
import io.cruvelo.operaty.db.TransactionalRunner
import io.ktor.server.config.ApplicationConfig
import jakarta.inject.Singleton
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

@Module
class Module {

    @Provides
    @Singleton
    fun provideDataSource(ktorAppConfig: ApplicationConfig): DataSource {
        val databaseConfigProvider = KtorEnvDatabaseConfigProvider(ktorAppConfig)
        return HikariCPDataSourceProvider(databaseConfigProvider, ktorAppConfig).provide()
    }

    @Provides
    @Singleton
    fun provideExposedDatabase(dataSource: DataSource): Database {
        val database = Database.connect(dataSource)
        return database
    }

    @Provides
    @Singleton
    fun provideTransactionRunner(database: Database): TransactionalRunner {
        return ExposedTransactionalRunner(database)
    }
}
