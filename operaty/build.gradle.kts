plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.ktor)
	alias(libs.plugins.kotlin.plugin.serialization)
	alias(libs.plugins.kapt)
}

group = "io.cruvelo"
version = "0.0.1"

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
	testImplementation(libs.ktor.server.test.host)
	testImplementation(libs.kotlin.test.junit)
}
