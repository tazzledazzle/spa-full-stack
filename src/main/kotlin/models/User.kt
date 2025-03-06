import kotlinx.serialization.Serializable

//package com.northshore.models
//
//import jakarta.validation.constraints.*
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class User(
//    @field:NotBlank(message = "Name is required")
//    val name: String = "",
//
//    @field:Email(message = "Please provide a valid email address")
//    @field:NotBlank(message = "Email is required")
//    val email: String = "",
//
//    @field:NotBlank(message = "Password is required")
//    @field:Size(min = 6, message = "Password must be at least 6 characters long")
//    val password: String = "",
//
//    @field:Min(value = 18, message = "Age must be 18 or older")
//    val age: Int? = null,
//
//    val gender: String = "",
//
//    val newsletter: Boolean = false,
//
//    @field:NotBlank(message = "Country is required")
//    val country: String = "",
//
//    val interests: List<String> = emptyList(),
//
//    val bio: String = ""
//)
//
//@Serializable
//data class Task(
//    @field:NotBlank(message = "Task name is required")
//    val name: String = "",
//
//    @field:NotBlank(message = "Task description is required")
//    val description: String = "",
//
//    @field:NotNull(message = "Task priority is required")
//    val priority: Priority? = null
//)
//
//@Serializable
//enum class Priority {
//    LOW, MEDIUM, HIGH
//}

@Serializable
data class User(val username: String? = null, val password: String? = null)

@Serializable
data class UserSession(val name: String, val count: Int)

