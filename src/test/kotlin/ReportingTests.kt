package com.northshore

import com.northshore.services.ProjectReportGenerator
import io.kotest.core.spec.style.DescribeSpec
import java.io.File
import kotlin.test.assertEquals

class ReportingTests: DescribeSpec ({
    val generator = ProjectReportGenerator()
    describe("Reporting") {
        it("should be able to generate a report") {
            val projectEntries = generator.getEntriesFromExcel(file = File("src/main/resources/Test6446Job.xlsx"))
            val projectData = generator.extractProjectDataFromEntries(projectEntries)
            val projectName = projectEntries.first().projectId
            val report = File("build/reports/Project-$projectName-${projectData.startDate}-${projectData.endDate}.pdf")
            report.writeBytes(generator.generateProjectReport(
                projectData = projectData,
                timesheetEntries = projectEntries
                ))
            assertEquals(true, true)
        }

    }
})