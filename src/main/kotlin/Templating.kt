//package com.northshore
//
//import io.ktor.http.*
//import io.ktor.server.application.*
//import io.ktor.server.html.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import io.ktor.server.thymeleaf.*
//import kotlinx.css.*
//import kotlinx.html.*
//import org.thymeleaf.TemplateEngine
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
//import java.util.Locale
//import org.thymeleaf.context.Context
//import org.thymeleaf.templatemode.TemplateMode
//
//fun Application.configureTemplating() {
//    install(Thymeleaf) {
//        setTemplateResolver(ClassLoaderTemplateResolver().apply {
//            prefix = "templates/thymeleaf/"
//            suffix = ".html"
//            characterEncoding = "utf-8"
//        })
//    }
//    routing {
//        get("/html-dsl") {
//            call.respondHtml {
//                body {
//                    h1 { +"HTML" }
//                    ul {
//                        for (n in 1..10) {
//                            li { +"$n" }
//                        }
//                    }
//                }
//            }
//        }
//        get("/styles.css") {
//            call.respondCss {
//                body {
//                    backgroundColor = Color.darkBlue
//                    margin(0.px)
//                }
//                rule("h1.page-title") {
//                    color = Color.white
//                }
//            }
//        }
//
//        get("/html-css-dsl") {
//            call.respondHtml {
//                head {
//                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
//                }
//                body {
//                    h1(classes = "page-title") {
//                        +"Hello from Ktor!"
//                    }
//                }
//            }
//        }
//        get("/html-thymeleaf") {
//            call.respond(ThymeleafContent("index", mapOf("user" to ThymeleafUser(1, "user1"))))
//        }
//    }
//}
//
//suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
//    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
//}
//
//data class ThymeleafUser(val id: Int, val name: String)
//
//
//suspend fun ApplicationCall.respondThymeleafTemplate(template: String, model: Map<String, Any>, thymeleaf: ThymeleafRenderer) =
//    thymeleaf.render(this, template, model)
//
//
//class ThymeleafRenderer {
//    private val templateEngine: TemplateEngine
//
//    init {
//        val resolver = ClassLoaderTemplateResolver().apply {
//            prefix = "templates/thymeleaf/"
//            suffix = ".html"
//            characterEncoding = "UTF-8"
//            templateMode = TemplateMode.HTML
//            // Cache templates in production, set to false for development
//            isCacheable = false
//        }
//
//        templateEngine = TemplateEngine().apply {
//            setTemplateResolver(resolver)
//        }
//    }
//
//    suspend fun render(call: ApplicationCall, template: String, model: Map<String, Any> = emptyMap()) {
//        val context = Context(Locale.getDefault(), model.toMutableMap())
//        val content = templateEngine.process(template, context)
//        call.respondText(content, ContentType.Text.Html, HttpStatusCode.OK)
//    }
//}