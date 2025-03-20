package com.northshore

import com.northshore.models.MasterExcelEntry
import com.northshore.services.ProjectReportGenerator
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.letsPlot.awt.util.RGBEncoderAwt
import org.jetbrains.letsPlot.commons.geometry.DoubleVector
import org.jetbrains.letsPlot.coord.coordFlip
import org.jetbrains.letsPlot.core.util.PlotSvgExportCommon
import org.jetbrains.letsPlot.geom.geomBar
import org.jetbrains.letsPlot.intern.StatKind
import org.jetbrains.letsPlot.intern.layer.StatOptions
import org.jetbrains.letsPlot.intern.toSpec
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.scale.scaleFillBrewer
import org.jetbrains.letsPlot.themes.elementBlank
import org.jetbrains.letsPlot.themes.theme
import org.jetbrains.letsPlot.tooltips.layerTooltips
import java.io.File
import java.io.FileInputStream
import kotlin.test.Test
import kotlin.test.assertEquals

private val payrollCategoryId = mapOf(
    1 to "SIDING",
    2 to "METAL ROOFING",
    3 to "ROOFING BUILD UP",
    4 to "GUTTERS & DS",
    5 to "DRIVING",
    6 to "SHOP / OFFICE"
)

class ApplicationTest {
    private val generator = ProjectReportGenerator()

    @Test
    fun `should return OK status when accessing projects endpoint`() = testApplication {
        application {
            module()
        }

        client.get("/projects").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun `should extract data from excel file and generate report`() {
        val testFile = File("src/main/resources/Test6446Job.xlsx")
        testFile.inputStream().use { stream ->
            val entryList = generator.getEntriesFromExcel(testFile)
            val projectData = generator.extractProjectDataFromEntries(entryList)
            val report = File("build/reports/Project-${projectData.name}-${projectData.startDate}-${projectData.endDate}.pdf")
            report.writeBytes(generator.generateProjectReport(projectData, entryList))
        }
    }

    @Test
    fun `should generate bar chart from project data`() {
        val testFile = File("src/main/resources/Test6446Job.xlsx")
        testFile.inputStream().use { stream ->
            val projectEntries = generator.getEntriesFromExcel(testFile)
            val taskData = getTaskData(projectEntries)
            val plotData = mapOf(
                "category" to taskData.keys.toList(),
                "value" to taskData.values.toList()
            )
            val result = generator.createBarChart(plotData)
            val plotBytes = PlotSvgExportCommon.buildSvgImageFromRawSpecs(
                plotSpec = result.toSpec(),
                plotSize = DoubleVector(500.0, 500.0),
                rgbEncoder = RGBEncoderAwt(),
                useCssPixelatedImageRendering = true
            )
//            FileInputStream(testFile).use { stream -> stream.readAllBytes() }
            assertEquals(true, plotBytes.isNotEmpty())
        }
    }

    @Test
    fun `should write chart bytes to file`() {
        val testFile = File("src/main/resources/Test6446Job.xlsx")
        testFile.inputStream().use { stream ->
            val projectEntries = generator.getEntriesFromExcel(testFile)
            val projectData = generator.extractProjectDataFromEntries(projectEntries)
            val projectName = projectEntries.last().jobName
            val report = File("build/reports/Project-$projectName-${projectData.startDate}-${projectData.endDate}.pdf")

            val taskData = getTaskData(projectEntries)
            val plotData = mapOf(
                "category" to taskData.keys.toList(),
                "value" to taskData.values.toList()
            )

            val result = letsPlot(plotData) +
                    geomBar(
                        stat = StatOptions(StatKind.IDENTITY),
                        alpha = 0.8,
                        fill = "white",
                        tooltips = layerTooltips()
                            .line("@category")
                            .line("Value: @value")
                    ) {
                        x = "category"
                        y = "value"
                    } +
                    ggtitle("Category Values") +
                    theme(line = elementBlank()) +
                    scaleFillBrewer(palette = "Set1") +
                    coordFlip()

            report.writeBytes(
                PlotSvgExportCommon.buildSvgImageFromRawSpecs(
                    plotSpec = result.toSpec(),
                    plotSize = DoubleVector(500.0, 500.0),
                    rgbEncoder = RGBEncoderAwt(),
                    useCssPixelatedImageRendering = true
                ).encodeToByteArray()
            )
        }
    }


    private fun getTaskData(projectEntries: List<MasterExcelEntry>) = projectEntries
        .groupBy { it.taskName }
        .mapValues { (_, entries) -> entries.sumOf { it.hoursWorked.toDoubleOrNull() ?: 0.0 } }

    data class TaskEntryWithEstimate(
        var taskName: String? = null,
        var PayrollCategoryId: String? = null,
        var TotalProjectedManhours: Double? = null,
        var TotalSpentHours: Double? = null
    )

    data class PayrollCategoryId(
        var id: Long? = null,
        var name: String? = null
    )
}