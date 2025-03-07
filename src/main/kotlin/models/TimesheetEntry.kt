package com.northshore.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TimesheetEntry(
    var id: Long? = null,
    var taskId: Long = 0L, // todo: Task Object
    var employeeName: String = "",
    var username: String = "",
    var hoursWorked: Double = 0.0,
    var weekStartDate: LocalDate,


    var workDate: LocalDate? = null,
    var notes: String? = "",
    var submittedAt: LocalDateTime? = null
)