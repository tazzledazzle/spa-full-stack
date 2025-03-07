package com.northshore.services

import com.northshore.com.northshore.models.TaskPriority
import com.northshore.models.Task
import com.northshore.models.TaskStatus


class TaskService {
    fun getTasks(): List<Task> {
        return listOf(
            Task(
                id = 1,
                projectId = 1,
                name = "Design mockups",
                description = "Create visual mockups for homepage and key sections",
                priority = TaskPriority.HIGH
            ),
            Task(
                id = 2,
                projectId = 1,
                name = "Frontend implementation",
                description = "Implement responsive HTML/CSS based on approved designs",
                priority = TaskPriority.MEDIUM
            ),
            Task(
                id = 3,
                projectId = 1,
                name = "Backend integration",
                description = "Connect frontend to CMS and databases",
                priority = TaskPriority.HIGH
            ),
            Task(
                id = 4,
                projectId = 2,
                name = "Requirements gathering",
                description = "Document functional and non-functional requirements",
                priority = TaskPriority.CRITICAL
            ),
            Task(
                id = 5,
                projectId = 2,
                name = "UI/UX design",
                description = "Design user interface with focus on usability",
                priority = TaskPriority.HIGH
            ),
            Task(
                id = 6,
                projectId = 2,
                name = "iOS development",
                description = "Develop native iOS application using Swift",
                priority = TaskPriority.MEDIUM
            )
        )
    }
}