package com.northshore.models

import kotlinx.serialization.Serializable

@Serializable
enum class ProjectStatus {
    COMPLETED,
    DELAYED,
    AT_RISK,
    ON_GOING
}