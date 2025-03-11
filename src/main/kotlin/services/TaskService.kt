package com.northshore.services

import com.northshore.com.northshore.models.TaskPriority
import com.northshore.models.Task
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import javax.sql.DataSource


class TaskService {
    private lateinit var db: Connection
    init {
        val props = Properties()
        props.load(TaskService::class.java.getResourceAsStream("/postgres.properties"))
        props.keys.forEach {
            println(it)
        }
         db = DriverManager.getConnection(props["db.url"].toString())
    }
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

    fun getTaskById(id: Long): Task {
        val resultSet = db.createStatement().executeQuery("select * from task where id = :id")
        resultSet.getObject("task").toString()

        return Task (
            id = resultSet.getLong("id"),
            projectId = resultSet.getLong("projectId"),
            name = resultSet.getString("name"),
            description = resultSet.getString("description"),
            estimatedHours = resultSet.getDouble("estimated_hours"),
            progress = resultSet.getDouble("progress"),
            createdAt = resultSet.getString("created_at"),
            priority = TaskPriority.valueOf(resultSet.getString("priority"))
        )
    }
}