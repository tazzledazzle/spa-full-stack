package com.northshore.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class Task (
    var id: Long? = null,

    var projectId: Long = 0L,

    var parentProject: Project? = null,

    var name: String = "",
    var description: String = "",
    var estimatedHours: Double = 0.0,
    /**
     * The percentage of the task that has been completed.
     * This is a value between 0 and 100.
     */
    var progress: Double? = 0.0,

    var priority: Priority = Priority.MEDIUM,

    var taskCreatedAt: LocalDateTime? = null,

) {
    fun setParentProjectForTask(project: Project) {
        parentProject = project
        setProjectIdForTask(project.id)
    }

    fun setProjectIdForTask(projectId: Long) {
        this.projectId = projectId
    }
}