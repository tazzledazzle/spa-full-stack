package com.northshore

import com.northshore.models.MasterExcelEntry
import com.northshore.services.ProjectReportGenerator
import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDateTime
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import kotlin.random.Random
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

//        client.get("/test").apply {
//            assertEquals(HttpStatusCode.OK, status)
//        }


//        client.get("/dashboard").apply {
//            assertEquals(HttpStatusCode.OK, status)
//        }

        client.get("/projects").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Ignore
    fun testExcelExtraction() {
        val excel = File("src/main/resources/Copy of Master FieldWorkLog.xlsx")
        println(excel.parent)
        val workbook = WorkbookFactory.create(excel.inputStream())
        val sheet = workbook.getSheetAt(0)
        val row = workbook.getSheetAt(0).asIterable()
        val entryList: MutableList<MasterExcelEntry> = mutableListOf()
        row.forEach { r ->
            var cell = r.iterator()
            val workerName = if (cell.hasNext()) cell.next().toString() else ""
            val userCode = if (cell.hasNext()) cell.next().toString() else ""
            var taskId = if (cell.hasNext()) cell.next().toString() else ""
            if (taskId != "") {
                println(taskId)
                taskId = when (taskId) {
                    "1", "1.0" -> {
                        "SIDING"
                    }
                    "2", "2.0" -> {
                        "METAL ROOFING"
                    }
                    "3", "3.0" -> {
                        "ROOFING BUILD UP"
                    }
                    "4", "4.0" -> {
                        "GUTTERS & DOWNSPOUTS"
                    }
                    "5", "5.0" -> {
                        "DRIVING"
                    }
                    "6", "6.0" -> {
                        "SHOP / OFFICE"
                    }
                    else -> {
                        "UNABLE_TO_PARSE"
                    }
                }
            }
            val hoursWorked = if (cell.hasNext()) cell.next().toString() else ""
            val overTime = if (cell.hasNext()) cell.next().toString() else ""
            val dateOfWork = if (cell.hasNext()) cell.next().toString() else ""
            val projectNumber = if (cell.hasNext()) cell.next().toString() else ""
            val shiftType = if (cell.hasNext()) cell.next().toString() else ""
            val verified = if (cell.hasNext()) cell.next().toString() else ""
            val isVerifiedForeman = if (cell.hasNext()) cell.next().toString() else ""
            val entry = MasterExcelEntry(
                workerName = workerName,
                userCode = userCode,
                taskId = taskId,
                hoursWorked = hoursWorked,
                overTime = overTime,
                dateOfWork = dateOfWork,
                projectNumber = projectNumber,
                shiftType = shiftType,
                verified = verified,
                isVerifiedForeman = isVerifiedForeman
            )
            entryList.add(entry)
        }


        val entriesByProject = entryList.groupBy { it.projectNumber }
//        entriesByProject.keys.forEach { key ->
        val key = entriesByProject.keys.first()
            val entries = entriesByProject[key]!!
            val projectData = extractProjectDataFromEntries(key, entries)
            val report = File("build/reports/Project-$key-${projectData.startDate}-${projectData.endDate}.pdf")
            report.writeBytes(ProjectReportGenerator().generateProjectReport(projectData, entryList))
//        }
//        val entriesByForeman = entryList.groupBy { it.foreman }
//        entriesByForeman.keys.forEach { key ->
//            println("Foreman :$key, Entries: ${entriesByForeman[key]?.size}")
//        }
//        val entriesByTask = entryList.groupBy { it.taskId }
//        entriesByTask.keys.forEach { key ->
//            println("TaskId :$key, Entries: ${entriesByTask[key]?.size}")
//        }
//        val entriesByUserCode = entryList.groupBy { it.userCode }
//        entriesByUserCode.keys.forEach { key ->
//            println("UserId :$key, Entries: ${entriesByUserCode[key]?.size}")
//        }
    }

    @Test
    fun testUpdateAndPopulateFromData() {
        val tasksToHours = mutableMapOf(
            "penthouse siding and coping" to 8.0,
            "roof screen wall" to 8.0,
            "L4 deck panels" to 8.0,
            "L10 deck panels" to 8.0,
            "column wraps @ podium" to 8.0,

        )
    }

    private fun extractProjectDataFromEntries(
        key: String,
        entries: List<MasterExcelEntry>
    ): ProjectReportGenerator.ProjectData {
        val startDate = entries.minOf { it.dateOfWork }
        val endDate = entries.maxOf { it.dateOfWork }
        return ProjectReportGenerator.ProjectData(
            id = key,
            name = key,
            projectManager = entries.first().foreman,
            startDate = startDate,
            endDate = endDate,
            status = "",
            completionPercentage = 25, //todo
            totalBudgetHours = 1000.0, //todo
            totalHoursLogged = entries.sumOf { it.hoursWorked.toDoubleOrNull() ?: 0.0 },

            )
    }

}
