package com.northshore

import com.northshore.module
import com.northshore.services.ProjectService
import com.northshore.services.TaskService
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.pebble.Pebble
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.netty.handler.logging.LogLevel
import io.pebbletemplates.pebble.loader.ClasspathLoader
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import java.io.File

fun main() {
 embeddedServer(Netty, port = 8081, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger(org.koin.core.logger.Level.DEBUG)
        modules(appModule)
    }

    install(CallLogging) {
        level = Level.INFO
    }

    install(DefaultHeaders)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    install(Pebble) {
        loader(ClasspathLoader().apply {
            prefix = "templates"
        })
    }

    routing {
        staticFiles("/static", File("src/main/resources/static"))
        staticResources("/resources", "static")
    }

    registerDashboardRoutes()
}

val appModule = module() {
    single { ProjectService() }
    single { TaskService() }
}