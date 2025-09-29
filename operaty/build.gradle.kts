plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.ktor)
	alias(libs.plugins.kotlin.plugin.serialization)
	alias(libs.plugins.kapt)
	alias(libs.plugins.flyway.db)
}

group = "io.cruvelo"
version = "0.0.1"

buildscript {
	dependencies {
		classpath("org.flywaydb:flyway-database-postgresql:11.10.2")
		classpath("org.postgresql:postgresql:42.7.7")
	}
}

application {
	mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.ktor.server.core)
	implementation(libs.ktor.server.host.common)
	implementation(libs.ktor.server.status.pages)
	implementation(libs.ktor.server.content.negotiation)
	implementation(libs.ktor.serialization.kotlinx.json)
	implementation(libs.ktor.server.netty)
	implementation(libs.ktor.client.core)
	implementation(libs.ktor.client.cio)
	implementation(libs.ktor.client.logging)
	implementation(libs.ktor.client.logging)
	implementation(libs.ktor.client.content.negotiation)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.pdf.box)
	implementation(libs.logback.classic)
	implementation(libs.mu.logging)
	implementation(libs.dagger)
	kapt(libs.dagger.compiler)
	implementation(libs.postgresql)
	implementation(libs.exposed.core)
	implementation(libs.exposed.dao)
	implementation(libs.exposed.jdbc)
	implementation(libs.exposed.java.time)
	implementation(libs.exposed.json)
	implementation(libs.hikari.cp)
	testImplementation(libs.ktor.server.test.host)
	testImplementation(libs.kotlin.test.junit)
	testImplementation(libs.mockk)
	testImplementation(libs.assertj.core)
}


flyway {
	driver = "org.postgresql.Driver"
	url = getEnvOrProperty("FLYWAY_DB_URL") ?: "jdbc:postgresql://localhost:5432/operaty_ai"
	user = getEnvOrProperty("FLYWAY_DB_USER") ?: "operaty_ai"
	password = getEnvOrProperty("FLYWAY_DB_PASSWORD") ?: "operaty_ai"
	baselineOnMigrate = true
}

fun getEnvOrProperty(value: String): String? = System.getenv(value) ?: System.getProperty(value)
