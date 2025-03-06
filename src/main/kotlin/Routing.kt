package com.northshore

import com.northshore.models.Priority
import com.northshore.models.Product
import com.northshore.models.Task
import com.northshore.models.User
import com.northshore.util.ValidationUtil
import io.ktor.http.Parameters
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.sessions
import java.time.LocalDateTime

fun Application.configureRouting() {

    val renderer = ThymeleafRenderer()
    routing {
        get("/") {
            val model = mapOf("pageTitle" to "Hello, World!",
                "message" to "Hello, Thymeleaf!",
                "currentTime" to LocalDateTime.now()
            )

            call.respondThymeleafTemplate("hello_world", model, renderer)
        }

        // Products route
        get("/products") {
            val products = listOf(
                Product(1, "Laptop", 999.99, true),
                Product(2, "Phone", 699.99, true),
                Product(3, "Tablet", 349.99, false),
                Product(4, "Headphones", 159.99, true),
                Product(5, "Mouse", 25.99, false)
            )

            val categoryMap = mapOf(
                "electronics" to "Electronics",
                "clothing" to "Clothing",
                "books" to "Books",
                "home" to "Home & Kitchen"
            )

            val model = mapOf(
                "products" to products,
                "categories" to categoryMap,
                "company" to "TechShop",
                "discount" to 15
            )

            call.respondThymeleafTemplate("products", model, renderer)
        }

        get("/tasks") {
            val tasks = listOf(
//                Task("1", "Task 1", Priority.LOW),
//                Task("2", "Task 2", Priority.MEDIUM),
//                Task("3", "Task 3", Priority.HIGH),
                Task(id = 1, projectId = 1, name = "Task 1", description =  "Description 1", priority = Priority.LOW),
            )

            val model = mapOf(
                "tasks" to tasks
            )

            call.respondThymeleafTemplate("tasks", model, renderer)
        }

        post("/tasks") {
            val formParameters = call.receiveParameters()
            val tasks = mutableListOf<Task>(
//                Task(formParameters["name"].toString(),formParameters["description"].toString(),formParameters["priority"] as Priority)
                        Task(id = 1, projectId = 1, name = "Task 1", description =  "Description 1", priority = Priority.LOW)
            )

            val model = mapOf(
                "tasks" to tasks
            )

            call.respondThymeleafTemplate("tasks", model, renderer)
        }

        // Registration form
        get("/register") {
            // Get flash data if available
            val flashData: FlashData = call.sessions.get("FlashData") as FlashData
            call.sessions.clear("FlashData")
            staticResources("/task-ui", "task-ui")
            // Form data
            val user = flashData.formData ?: User()
            val errors = flashData.errors

            val model = mutableMapOf<String, Any>(
                "user" to user,
                "errors" to errors
            )

            // Add form options
            addFormData(model)

            call.respondThymeleafTemplate("register", model, renderer)
        }

        // Process form submission
        post("/register") {
            val formParameters = call.receiveParameters()

            // Parse parameters into User object
            val user = parseUserFromParameters(formParameters)

            // Validate user
            val errors = ValidationUtil.validate(user, User::class)

            if (errors.isEmpty()) {
                // Success - set flash data and redirect
                call.sessions.set("flashData",
                    FlashData(
                        successMessage = "Registration successful!",
                        registeredUser = user
                    ))
                call.respondRedirect("/success")
            } else {
                // Validation errors - set flash data and redirect back to form
                call.sessions.set("flashData", FlashData(
                    errors = errors,
                    formData = user
                ))
                call.respondRedirect("/register")
            }
        }

        // Success page
        get("/success") {
            val flashData = call.sessions.get("flashData") as FlashData
            call.sessions.clear("flashData")

            if (flashData.registeredUser == null) {
                call.respondRedirect("/register")
                return@get
            }

            val model = mapOf(
                "successMessage" to (flashData.successMessage ?: "Registration Successful!"),
                "registeredUser" to (flashData.registeredUser ?: User())
            )

            call.respondThymeleafTemplate("success", model, renderer)
        }
    }


}


// Helper function to add form data options
private fun addFormData(model: MutableMap<String, Any>) {
    model["countries"] = listOf(
        "United States", "United Kingdom", "Canada", "Australia", "Germany",
        "France", "Japan", "China", "Brazil", "India"
    )

    model["interestOptions"] = listOf(
        "Technology", "Music", "Sports", "Reading", "Travel",
        "Cooking", "Art", "Photography", "Gaming", "Fashion"
    )

    model["genderOptions"] = listOf(
        "Male", "Female", "Non-binary", "Prefer not to say"
    )
}

// Helper function to parse form parameters into User object
private fun parseUserFromParameters(params: Parameters): User {
    return User(
//        name = params["name"] ?: "",
        email = params["email"] ?: "",
        password = params["password"] ?: "",
//        age = params["age"]?.toIntOrNull(),
//        gender = params["gender"] ?: "",
//        newsletter = params["newsletter"] == "on" || params["newsletter"] == "true",
//        country = params["country"] ?: "",
//        interests = params.getAll("interests") ?: emptyList(),
//        bio = params["bio"] ?: ""
    )
}