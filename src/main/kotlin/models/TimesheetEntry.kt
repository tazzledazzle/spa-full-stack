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

data class MasterExcelEntry(
    var userCode: String = "",
    var taskName: String = "",
    var taskId: String = "",
    var hoursWorked: String = "",
    var overTime: String = "",
    var dateOfWork: LocalDate? = null,
    var projectJobId: String = "",
    var shiftType: String = "",
    var foremanName: String = "",
    var jobName: String = "",
    var estimatedHours: String = "",
    var isVerifiedForeman: String = ""
)
