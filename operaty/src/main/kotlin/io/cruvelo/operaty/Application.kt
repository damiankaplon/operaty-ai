package io.cruvelo.operaty

import io.cruvelo.operaty.report.road.RoadReportController
import io.cruvelo.operaty.report.road.RoadReportDto
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

private val LOGGER = KotlinLogging.logger {}

fun main(args: Array<String>) {
	EngineMain.main(args)
}

fun Application.module() {
	install(ContentNegotiation) {
		json(Json { ignoreUnknownKeys = true })
	}
	val appComponent = DaggerAppComponent.builder()
		.applicationConfig(environment.config)
		.build()
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			LOGGER.error(throwable = cause) { "Application internal error:" }
			call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
		}
	}
	val controller: RoadReportController = appComponent.roadReportController()
	routing {
		route("api") {
			route("reports") {
				route("road") {
					post {
						val multipart = call.receiveMultipart()
						controller.generate(multipart).let { call.respond(it) }
					}
					post("/version") {
						val report = call.receive(RoadReportDto::class)
						LOGGER.info { "Updating road report with: " }
						controller.update(report).let { call.respond(it) }
					}
					get {
						controller.getAll().let { call.respond(it) }
					}
				}
			}
		}
	}
}
