package com.northshore.models

import com.northshore.com.northshore.models.UserRole
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(
    var id: Long = 0L,
    var firstName: String = "",
    var lastName: String = "",
    var username: String = "",

    @field:NotBlank(message = "email cannot be blank")
    var email: String = "",
    var password: String = "",
    var role: UserRole = UserRole.FOREMAN, // foreman role only pushes data, more secure
    @field:NotBlank(message = "createdAt cannot be blank")
    var createdAt: String = LocalDateTime.now().toString()
)

@Serializable
enum class Priority {
    LOW, MEDIUM, HIGH, CRITICAL
}
