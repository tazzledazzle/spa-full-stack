package com.northshore.services

import com.northshore.models.Task


class TaskService {
    fun getTasks(): List<Task> {
        return listOf(
            Task(1, "Create a user flow of social application design", TaskStatus.APPROVED, true),
            Task(2, "Create a user flow of social application design", TaskStatus.IN_REVIEW, true),
            Task(3, "Landing page design for Fintech project of singapore", TaskStatus.IN_REVIEW, true),
            Task(4, "Interactive prototype for app screens of deltamine project", TaskStatus.ON_GOING, false),
            Task(5, "Interactive prototype for app screens of deltamine project", TaskStatus.APPROVED, true)
        )
    }
}