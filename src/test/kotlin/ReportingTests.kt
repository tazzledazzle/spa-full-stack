package com.northshore

import com.northshore.services.ProjectReportGenerator
import io.kotest.core.spec.style.DescribeSpec
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ReportingTests: DescribeSpec ({
    val generator = ProjectReportGenerator()
    describe("Reporting") {
        it("should be able to generate a report") {
            val projectEntries = generator.getEntriesFromExcel(file = File("src/main/resources/Test6446Job.xlsx"))
            val projectData = generator.extractProjectDataFromEntries(projectEntries)
            val projectName = projectEntries.last().jobName
            val report = File("build/reports/Project-$projectName-${projectData.startDate}-${projectData.endDate}.pdf")
            val reportBytes = generator.generateProjectReport(
                projectData = projectData,
                timesheetEntries = projectEntries
            )
            report.writeBytes(reportBytes)
            assertNotNull(reportBytes)
        }

    }
})