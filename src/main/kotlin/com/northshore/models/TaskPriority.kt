package com.northshore.com.northshore.models

import kotlinx.serialization.Serializable

@Serializable
enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}
