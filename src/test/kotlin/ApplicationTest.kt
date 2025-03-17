package com.northshore

import com.northshore.models.MasterExcelEntry
import com.northshore.services.ProjectReportGenerator
import com.northshore.services.ProjectReportGenerator.Companion.parseDateFromDateOfWork
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.util.*
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


val payrollCategoryId = mutableMapOf(
    1 to "SIDING",
    2 to "METAL ROOFING",
    3 to "ROOFING BUILD UP",
    4 to "GUTTERS & DS",
    5 to "DRIVING",
    6 to "SHOP / OFFICE",
)

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

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
//            val workerName = if (cell.hasNext()) cell.next().toString() else ""
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
            val dateOfWorkStr = if (cell.hasNext()) cell.next().toString() else ""
            val dateOfWork = parseDateFromDateOfWork(dateOfWorkStr)
            val projectNumber = if (cell.hasNext()) cell.next().toString() else ""
            val shiftType = if (cell.hasNext()) cell.next().toString() else ""
            val verified = if (cell.hasNext()) cell.next().toString() else ""
            val isVerifiedForeman = if (cell.hasNext()) cell.next().toString() else ""
            val entry = MasterExcelEntry(
                userCode = userCode,
                taskId = taskId,
                hoursWorked = hoursWorked,
                overTime = overTime,
                dateOfWork = dateOfWork,
                projectId = projectNumber,
                shiftType = shiftType,
                jobName = verified,
                isVerifiedForeman = isVerifiedForeman
            )
            entryList.add(entry)
        }


        val entriesByProject = entryList.groupBy { it.projectId }
        val key = entriesByProject.keys.first()
        val entries = entriesByProject[key]!!
        val projectData = ProjectReportGenerator().extractProjectDataFromEntries(entries)
        val report = File("build/reports/Project-$key-${projectData.startDate}-${projectData.endDate}.pdf")
        report.writeBytes(ProjectReportGenerator().generateProjectReport(projectData, entryList))
    }

    @Ignore
    fun testUpdateAndPopulateFromData() {
//        val entryList = getEntriesFromExcel(File("src/main/resources/Test6446Job.xlsx"))

//        val tasksToHours = File("src/main/resources/TaskToHours.csv").readLines().map {
//            val split = it.split(",")
//            TaskEntryWithEstimate(
//                taskName = split[0],
//                PayrollCategoryId = split[1],
//                TotalProjectedManhours = Random().nextDouble(1.0, 9.0),
//                TotalSpentHours = Random().nextDouble(1.0, 9.0)
//            )
//        }

        // need to take the entries from the csv and merge them with the taskentrywithestimate

//        val entriesByProject = entryList.groupBy { it.projectId }
//        val key = entriesByProject.keys.first()
//        val entries = entriesByProject[key]!!
//        val projectData = extractProjectDataFromEntries(key, entries)
//        val report = File("build/reports/Project-$key-${projectData.startDate}-${projectData.endDate}.pdf")
//        report.writeBytes(ProjectReportGenerator().generateProjectReport(projectData, entryList))

    }

    data class TaskEntryWithEstimate(
        var taskName: String? = null,
        var PayrollCategoryId: String? = null,
        var TotalProjectedManhours: Double? = null,
        var TotalSpentHours: Double? = null,
    )

    data class PayrollCategoryId(
        var id: Long? = null,
        var name: String? = null,
    )


}
