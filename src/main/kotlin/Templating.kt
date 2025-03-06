package com.northshore

import com.northshore.com.northshore.models.UserRole
import com.northshore.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.pebble.Pebble
import io.ktor.server.pebble.PebbleContent
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import io.pebbletemplates.pebble.loader.ClasspathLoader
import kotlinx.css.*
import kotlinx.html.*
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.util.Locale
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    install(Pebble) {
        loader(ClasspathLoader().apply {
            prefix = "templates/pebble/"
        })
    }
    routing {
        get("/users") {
            val users = listOf(
                User(1, firstName = "John", lastName = "Doe", username = "jdoe", email = "jdoe@company.com",
                    password = "password", role = UserRole.ADMIN, createdAt = "2025-03-06"),

                User(2, firstName = "Jane", lastName = "Smith", username = "jsmith", email = "jsmith@company.com",
                    password = "password", role = UserRole.PM, createdAt = "2025-03-06"),

                User(3, firstName = "Michael", lastName = "Johnson", username = "mjohnson", email = "mjohnson@company.com",
                    password = "password", role = UserRole.DEVELOPER, createdAt = "2025-03-06"),

                User(4, firstName = "Emily", lastName = "Williams", username = "ewilliams", email = "ewilliams@company.com",
                    password = "password", role = UserRole.TESTER, createdAt = "2025-03-06"),

                User(5, firstName = "David", lastName = "Brown", username = "dbrown", email = "dbrown@company.com",
                    password = "password", role = UserRole.ANALYST, createdAt = "2025-03-06"),

                User(6, firstName = "Sarah", lastName = "Miller", username = "smiller", email = "smiller@company.com",
                    password = "password", role = UserRole.DEVELOPER, createdAt = "2025-03-06"),

                User(7, firstName = "Robert", lastName = "Wilson", username = "rwilson", email = "rwilson@company.com",
                    password = "password", role = UserRole.PM, createdAt = "2025-03-06"),

                User(8, firstName = "Jessica", lastName = "Taylor", username = "jtaylor", email = "jtaylor@company.com",
                    password = "password", role = UserRole.TESTER, createdAt = "2025-03-06"),

                User(9, firstName = "Thomas", lastName = "Anderson", username = "tanderson", email = "tanderson@company.com",
                    password = "password", role = UserRole.DEVELOPER, createdAt = "2025-03-06"),

                User(10, firstName = "Lisa", lastName = "Garcia", username = "lgarcia", email = "lgarcia@company.com",
                    password = "password", role = UserRole.ADMIN, createdAt = "2025-03-06")
            )
            call.respond(PebbleContent("users.html", mapOf("users" to users, "websiteTitle" to "Pebble")))
        }

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.darkBlue
                    margin(0.px)
                }
                rule("h1.page-title") {
                    color = Color.white
                }
            }
        }

        get("/html-css-dsl") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                }
                body {
                    h1(classes = "page-title") {
                        +"Hello from Ktor!"
                    }
                }
            }
        }
        get("/html-thymeleaf") {
            call.respond(ThymeleafContent("index", mapOf("user" to ThymeleafUser(1, "user1"))))
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

data class ThymeleafUser(val id: Int, val name: String)


suspend fun ApplicationCall.respondThymeleafTemplate(template: String, model: Map<String, Any>, thymeleaf: ThymeleafRenderer) =
    thymeleaf.render(this, template, model)


class ThymeleafRenderer {
    private val templateEngine: TemplateEngine

    init {
        val resolver = ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "UTF-8"
            templateMode = TemplateMode.HTML
            // Cache templates in production, set to false for development
            isCacheable = false
        }

        templateEngine = TemplateEngine().apply {
            setTemplateResolver(resolver)
        }
    }

    suspend fun render(call: ApplicationCall, template: String, model: Map<String, Any> = emptyMap()) {
        val context = Context(Locale.getDefault(), model.toMutableMap())
        val content = templateEngine.process(template, context)
        call.respondText(content, ContentType.Text.Html, HttpStatusCode.OK)
    }
}