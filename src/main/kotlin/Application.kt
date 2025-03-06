package com.northshore

import UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.sessions
import io.ktor.util.reflect.*
import kotlinx.html.*

//fun main(args: Array<String>) {
//    io.ktor.server.netty.EngineMain.main(args)

//    embeddedServer(Netty, port = 8080, host = "localhost") {
//        install(Sessions) {
//            cookie<FlashData>("FLASH_DATA") {
//                cookie.path = "/cookies"
//                serializer = KotlinxSessionSerializer(Json {
//                    prettyPrint = true
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                })
//            }
//        }

//        main()
//    }.start(wait = true)}

fun Application.main() {
    install(Authentication) {
        session<UserSession>("auth-session") {
            validate { session ->
                if (session.name.startsWith("jet")) {
                    session
                }
                else {
                    null
                }
            }
            challenge { call.respondRedirect("/login") }
        }

        form("auth-form") {

            userParamName = "username"
            passwordParamName = "password"

            validate { credentials ->
                if (credentials.name == "jetbrains" && credentials.password == "foobar") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized, typeInfo<String>())
            }
        }
    }

    routing {
        authenticate("auth-form") {
            get("/hello") {
                val session = call.principal<UserSession>("user")
                call.sessions.set("user", session?.copy(count = session.count + 1))
                call.respondText("Hello, ${session?.name}! You've visited this page ${session?.count} times.")
            }
            post("/login") {
                val userName = call.principal<UserIdPrincipal>()?.name.toString()
                call.sessions.set("user", UserSession(name = userName, count =  1))
                call.respondRedirect("/hello")
            }
        }
        get("/login") {

            call.respondHtml {
                body {
                    form(action = "/login", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
                        p {
                            +"Username:  "
                            textInput(name = "username")
                        }
                        p {
                            +"Password:  "
                            textInput(name = "password")
                        }
                        p {
                            submitInput() { value = "Login" }
                        }
                    }
                }
            }
        }
    }
}


