package io.cruvelo.operaty.db

import io.ktor.server.config.ApplicationConfig

data class DatabaseConfig(
    val jdbcUrl: String,
    val user: String,
    val password: String,
    val schema: String = "public",
)

fun interface DatabaseConfigProvider {
    @Throws(IllegalStateException::class)
    fun provide(): DatabaseConfig
}

class KtorEnvDatabaseConfigProvider(
    private val ktorAppConfig: ApplicationConfig
) : DatabaseConfigProvider {
    override fun provide(): DatabaseConfig =
        DatabaseConfig(
            jdbcUrl = ktorAppConfig.property("app.db.jdbcUrl").getString(),
            user = ktorAppConfig.property("app.db.user").getString(),
            password = ktorAppConfig.property("app.db.password").getString(),
            schema = ktorAppConfig.property("app.db.schema").getString(),
        )
}
