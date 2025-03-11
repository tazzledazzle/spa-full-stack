package com.northshore.models

import com.northshore.com.northshore.models.TaskPriority
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Task (
    var id: Long? = null,

    var projectId: Long = 0L,
    var name: String = "",
    var description: String = "",
    var estimatedHours: Double = 0.0,
    /**
     * The percentage of the task that has been completed.
     * This is a value between 0 and 100.
     */
    var progress: Double? = 0.0,

    var createdAt: String? = null,
    var priority: TaskPriority = TaskPriority.LOW,
    var project: Project? = null
) {
    fun isCompleted(): Boolean {
        return progress == 100.0
    }
}