package com.northshore.services

import com.northshore.models.Project
import com.northshore.models.ProjectStatus
import com.northshore.models.User
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeFormatter

class ProjectService {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    fun getProjects(): List<Project> {
        return listOf(
            Project(
                id = 1,
                name = "Neisa web developement",
                projectManager = "Om prakash sao",
                dueDate = LocalDate.parse("2023-05-25"),
                status = ProjectStatus.COMPLETED,
                progress = 100
            ),
            Project(
                id = 2,
                name = "Datascale AI app",
                projectManager = "Neilsan mando",
                dueDate = LocalDate.parse("2023-06-20"),
                status = ProjectStatus.DELAYED,
                progress = 30
            ),
            Project(
                id = 3,
                name = "Media channel branding",
                projectManager = "Tiruvelly priya",
                dueDate = LocalDate.parse("2023-07-13"),
                status = ProjectStatus.AT_RISK,
                progress = 40
            ),
            Project(
                id = 4,
                name = "Corlax iOS app developement",
                projectManager = "Matte hannery",
                dueDate = LocalDate.parse("2023-12-20"),
                status = ProjectStatus.COMPLETED,
                progress = 100
            ),
            Project(
                id = 5,
                name = "Website builder developement",
                projectManager = "Sukumar rao",
                dueDate = LocalDate.parse("2024-03-15"),
                status = ProjectStatus.ON_GOING,
                progress = 50
            )
        )
    }

    fun getTeamMembers(): List<User> {
        return listOf(
            User(1, "Sam", "Designer", taskCount = 7),
            User(2, "Meldy", "Developer", taskCount = 8),
            User(3, "Ken", "Developer", taskCount = 2),
            User(4, "Dmitry", "QA", taskCount = 8),
            User(5, "Vego", "Designer", taskCount = 8),
            User(6, "Kadon", "Developer", taskCount = 2),
            User(7, "Malin", "PM", taskCount = 4)
        )
    }

    fun getCurrentUser(): User {
        return User(8, "Alex melan", "Product manager")
    }

    fun getDashboardStats(): DashboardStats {
        return DashboardStats(
            totalRevenue = 53_009.89,
            revenueIncrease = 12.0,
            projectCount = 95,
            projectTotal = 100,
            projectDecrease = 10.0,
            timeSpent = 1022,
            timeTotal = 1300,
            timeIncrease = 8.0,
            resourceCount = 101,
            resourceTotal = 120,
            resourceIncrease = 2.0,
            completedProjects = 26,
            delayedProjects = 35,
            ongoingProjects = 35,
            completionPercentage = 72
        )
    }

    fun formatDate(date: LocalDate): String {
        return when (date.month.value) {
            3 -> "Mar ${date.dayOfMonth}, ${date.year}"
            5 -> "May ${date.dayOfMonth}, ${date.year}"
            6 -> "Jun ${date.dayOfMonth}, ${date.year}"
            7 -> "July ${date.dayOfMonth}, ${date.year}"
            12 -> "Dec ${date.dayOfMonth}, ${date.year}"
            else -> date.format(dateFormatter)
        }
    }
}