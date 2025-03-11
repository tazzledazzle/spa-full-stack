package com.northshore

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

            println("Projects: $projects")
            println("Tasks: $tasks")
            println("Team members: $teamMembers")
            println("Current user: $currentUser")
            println("Stats: $stats")

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

        get("/projects/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
//            if (id == null) {
//                call.respondText("Invalid project ID", status = HttpStatusCode.BadRequest)
//                return@get
//            }

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
            TODO()
        }

        get("/time-log") {
            TODO()
        }


    }
}