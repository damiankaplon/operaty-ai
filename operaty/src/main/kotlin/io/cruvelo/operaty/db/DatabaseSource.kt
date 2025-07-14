package io.cruvelo.operaty.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import javax.sql.DataSource

fun interface DataSourceProvider {
    @Throws(IllegalStateException::class)
    fun provide(): DataSource
}

class HikariCPDataSourceProvider(
    private val databaseConfigProvider: DatabaseConfigProvider,
    private val ktorAppConfig: ApplicationConfig
) : DataSourceProvider {
    override fun provide(): DataSource {
        val dbConfig = databaseConfigProvider.provide()
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = dbConfig.jdbcUrl
            username = dbConfig.user
            password = dbConfig.password
            schema = dbConfig.schema
            maximumPoolSize = ktorAppConfig.propertyOrNull("ap.db.poolSize")?.getString()?.toInt() ?: 10
        }
        return HikariDataSource(hikariConfig)
    }
}
