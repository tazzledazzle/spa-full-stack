package com.northshore.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Project (
    var id: Long = 0L,

    var projectManagerId: Long = 0L, // todo: User Object

    var name: String = "",

    var description: String = "",

    var startDate:  LocalDateTime? = null,

    var endDate:  LocalDateTime? = null,

    var projectManager: User? = null,

    var createdAt: LocalDateTime? = null,

    var status: ProjectStatus = ProjectStatus.ON_GOING,
    var progress: Double = 0.0,

    var tasks: List<Task> = mutableListOf() )
{
    fun addTask(task: Task) {
        tasks += task
        task.projectId = this.id
    }

    fun removeTask(task: Task) {
        tasks -= task
        task.projectId = 0L
    }
}