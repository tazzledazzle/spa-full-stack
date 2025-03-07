package com.northshore

import com.northshore.models.ProjectStatus
import com.northshore.models.TaskStatus
import com.northshore.services.ProjectService
import com.northshore.services.TaskService
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import org.koin.ktor.ext.inject

fun Application.registerDashboardRoutes() {
    val projectService by inject<ProjectService>()
    val taskService by inject<TaskService>()

    routing {
        get("/") {
            call.respondRedirect("/dashboard")
        }
        get("/dashboard") {
            val projects = projectService.getProjects()

            println("Projects: $projects")
            val tasks = taskService.getTasks()
            println("Tasks: $tasks")
            val teamMembers = projectService.getTeamMembers()
            println("Team members: $teamMembers")
            val currentUser = projectService.getCurrentUser()
            println("Current user: $currentUser")
            val stats = projectService.getDashboardStats()
            println("Stats: $stats")

            val model = mapOf(
                "pageTitle" to "NorthShore Dashboard",
                "projects" to projects,
                "tasks" to tasks,
                "teamMembers" to teamMembers,
                "currentUser" to currentUser,
                "stats" to stats,
                "projectStatutses" to ProjectStatus.entries.toTypedArray(),
                "taskStatuses" to TaskStatus.entries.toTypedArray(),
                "formatDate" to {date: Any ->
                    if (date is LocalDate) {
                        projectService.formatDate(date)
                    }
                    else {
                        date.toString()
                    }
                }
            )

            call.respond(PebbleContent("dashboard.peb", model))
        }

        get("/test") {
            call.respondText("Hello, World!")
        }
    }
}