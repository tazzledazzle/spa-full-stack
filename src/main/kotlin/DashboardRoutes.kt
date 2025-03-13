package com.northshore

import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfString
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfViewerPreferences
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.List
import com.itextpdf.layout.element.ListItem
import com.itextpdf.layout.element.Paragraph
import com.northshore.models.ProjectStatus
import com.northshore.models.TaskStatus
import com.northshore.services.ProjectService
import com.northshore.services.TaskService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.registerDashboardRoutes() {
    val projectService by inject<ProjectService>()
    val taskService by inject<TaskService>()

    routing {
        get("/") {
            call.respondRedirect("/dashboard")
        }
        get("/dashboard") {
            val projects = projectService.getProjectsFromService()
            val tasks = taskService.getTasks()
            val teamMembers = projectService.getTeamMembers()
            val currentUser = projectService.getCurrentUser()
            val stats = projectService.getDashboardStats()


            val model = mapOf(
                "pageTitle" to "NorthShore Dashboard",
                "projects" to projects,
                "tasks" to tasks,
                "teamMembers" to teamMembers,
                "currentUser" to currentUser,
                "stats" to stats,
                "projectStatutses" to ProjectStatus.entries.toTypedArray(),
                "taskStatuses" to TaskStatus.entries.toTypedArray()
            )

            call.respond(PebbleContent("dashboard.peb", model))
        }

        get("/test") {
            call.respondText("Hello, World!")
        }

        get("/projects") {
            val projects = projectService.getProjectsFromService()
            val model = mapOf(
                "pageTitle" to "Projects",
                "projects" to projects
            )

            call.respond(PebbleContent("projects.peb", model))
        }

        get("/project-dashboard") {
            call.respond(PebbleContent(
                "progress-dashboard.peb",
                mapOf(
                    "designProgress" to 75,
                    "devProgress" to 45,
                    "testingProgress" to 20
                )
            ))
        }

        get("/projects/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)

            val project = projectService.getProjectById(id)
            if (project == null) {
                call.respondText("Project not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val model = mapOf(
                "pageTitle" to project.name,
                "project" to project
            )

            call.respond(PebbleContent("project.peb", model))
        }

        get("/tasks") {
            val tasks = taskService.getTasks()
            val model = mapOf(
                "pageTitle" to "Tasks",
                "tasks" to tasks
            )
            call.respond(PebbleContent("tasks.peb", model))
        }

        get("/time-log") {
            val entries = projectService.getTimeLogEntries()
        }

        get("/reports") {
            // extracted data
            extractDataFromMasterDatabase()
            // filter by query
            // generate and render report
            call.respond(HttpStatusCode.OK)
        }

    }
}

private fun extractDataFromMasterDatabase() {
    val pdfDoc = PdfDocument(PdfWriter("results/report.pdf", WriterProperties().addXmpMetadata().setPdfVersion(PdfVersion.PDF_2_0)))
    val document = Document(pdfDoc, PageSize.A4.rotate())

    pdfDoc.setTagged()

    pdfDoc.catalog.setViewerPreferences(PdfViewerPreferences().setDisplayDocTitle(true))
    pdfDoc.catalog.setLang(PdfString("en-US"))
    val info = pdfDoc.documentInfo
    info.title = "NorthShore Report"

    val paragraph = Paragraph()

    paragraph.add("NorthShore Report")

    val img = Image(ImageDataFactory.create("https://backendstack-filestoragebucket10e371b0-mnfpuelz7fpb.s3.us-west-2.amazonaws.com/users/user-123/465a1993-5234-4856-a03e-434622a276bf-App-Runner.svg"))
    img.accessibilityProperties.alternateDescription = "App Runner Logo"
    paragraph.add(img)


    document.add(paragraph)

    val font = PdfFontFactory.createFont(PdfEncodings.WINANSI, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED)
    val list = List().setFont(font)
    paragraph.setFont(font)
    list.add(ListItem("1"))
    list.add(ListItem("2"))
    list.add(ListItem("3"))
    document.close()
}
