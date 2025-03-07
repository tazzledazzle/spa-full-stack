package com.northshore.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: Long = 0L,
    var firstName: String = "",
    var lastName: String = "",
    var username: String = "",

    var email: String = "",
    var password: String = "",
    var role: UserRole = UserRole.FOREMAN, // foreman role only pushes data, more secure
    var createdAt: LocalDateTime? = null
)