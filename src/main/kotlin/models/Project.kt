package com.northshore.models

import jakarta.validation.constraints.NotBlank
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class Project (
    @field:NotBlank(message = "Project ID cannot be empty")
    var id: Long = 0L,

    @field:NotBlank(message = "username cannot be empty")
    var name: String = "",

    var description: String = "",

    var startDate:  LocalDateTime? = null,

    var endDate:  LocalDateTime? = null,

    var projectManager: User? = null,

    var createdAt: LocalDateTime? = null,

    var tasks: List<Task> = mutableListOf()
) {
    fun addTask(task: Task) {
        if (task.projectId == id) {
            tasks += task
        }
        tasks += task
    }

    fun removeTask(task: Task) {
        tasks -= task
    }
}