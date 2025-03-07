package com.northshore.models

data class DashboardStats(
    val totalRevenue: Double,
    val revenueIncrease: Double,
    val projectCount: Int,
    val projectTotal: Int,
    val projectDecrease: Double,
    val timeSpent: Int,
    val timeTotal: Int,
    val timeIncrease: Double,
    val resourceCount: Int,
    val resourceTotal: Int,
    val resourceIncrease: Double,
    val completedProjects: Int,
    val delayedProjects: Int,
    val ongoingProjects: Int,
    val completionPercentage: Int
)