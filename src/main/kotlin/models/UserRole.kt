package com.northshore.models

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole(var role: String) {
    ADMIN("ADMIN"),
    PROJECT_MANAGER("PROJECT_MANAGER"),
    DEVELOPER("DEVELOPER"),
    FOREMAN ("FOREMAN"),
    ANALYST("ANALYST"),
    TESTER("TESTER")
}
