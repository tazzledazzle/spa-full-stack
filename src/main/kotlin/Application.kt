package com.northshore

import com.northshore.models.User
import com.northshore.util.ValidationUtil
import io.ktor.http.*
import io.ktor.http.ContentType.Application.FormUrlEncoded
import io.ktor.http.ContentType.Application.Json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.sessions.serialization.KotlinxSessionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

fun main(args: Array<String>) {
//    io.ktor.server.netty.EngineMain.main(args)

    embeddedServer(Netty, port = 8080, host = "localhost") {
        install(Sessions) {
            cookie<FlashData>("FLASH_DATA") {
                cookie.path = "/cookies"
                serializer = KotlinxSessionSerializer(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }

        module()
    }.start(wait = true)}

@Serializable
data class FlashData(
    var successMessage: String? = null,
    var registeredUser: User? = null,
    var errors: Map<String, String> = emptyMap(),
    var formData: User? = null
)
fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureTemplating()
    configureDatabases()
    configureRouting()
}


