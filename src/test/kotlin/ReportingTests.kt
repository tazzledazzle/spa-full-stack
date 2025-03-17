package com.northshore

import com.northshore.models.MasterExcelEntry
import com.northshore.services.ProjectReportGenerator
import com.northshore.services.ProjectReportGenerator.ProjectData
import io.kotest.core.spec.style.DescribeSpec
import java.io.File
import kotlin.test.assertEquals

class ReportingTests: DescribeSpec ({
    val generator = ProjectReportGenerator()
    describe("Reporting") {
        it("should be able to generate a report") {
            val projectData = generator.extractProjectDataFromEntries(file = File("src/test/resources/Test6446Job.xlsx"))
            val projectEntries = generator.getEntriesFromExcel(file = File("src/test/resources/Test6446Job.xlsx"))
            generator.generateProjectReport(
                projectData = projectData,
                timesheetEntries = projectEntries
                )
            assertEquals(true, true)
        }

    }
})