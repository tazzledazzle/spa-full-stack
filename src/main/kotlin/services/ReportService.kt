package com.northshore.services

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.BorderRadius
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.northshore.models.MasterExcelEntry
import kotlinx.datetime.Clock.System
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.jetbrains.letsPlot.batik.plot.component.DefaultPlotPanelBatik
import org.jetbrains.letsPlot.coord.coordFlip
import org.jetbrains.letsPlot.core.plot.builder.presentation.Defaults
import org.jetbrains.letsPlot.core.util.MonolithicCommon
import org.jetbrains.letsPlot.geom.geomBar
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.intern.StatKind
import org.jetbrains.letsPlot.intern.layer.StatOptions
import org.jetbrains.letsPlot.intern.toSpec
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.scale.scaleFillBrewer
import org.jetbrains.letsPlot.themes.elementBlank
import org.jetbrains.letsPlot.themes.theme
import org.jetbrains.letsPlot.tooltips.layerTooltips
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PiePlot
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.general.DefaultPieDataset
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import javax.swing.JPanel

class ProjectReportGenerator {

    // Chart colors
    private val CHART_COLORS = listOf(
        Color(0, 136, 254),    // #0088FE
        Color(0, 196, 159),    // #00C49F
        Color(255, 187, 40),   // #FFBB28
        Color(255, 128, 66),   // #FF8042
        Color(136, 132, 216),  // #8884d8
        Color(130, 202, 157)   // #82ca9d
    )

    /**
     * Generate a complete project timesheet report as a PDF
     *
     * @param projectData Project metadata
     * @param timesheetEntries List of timesheet entries
     * @return ByteArray containing the PDF document
     */
    fun generateProjectReport(
        projectData: ProjectData,
        timesheetEntries: List<MasterExcelEntry>
    ): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val pdfDocument = PdfDocument(PdfWriter(outputStream))
        pdfDocument.defaultPageSize = PageSize.A4

        val document = Document(pdfDocument)

        pdfDocument.defaultPageSize = PageSize.A4

        try {
            // 1. Add report header
            addReportHeader(document)

            // 2. Add project information
            addProjectInformation(document, projectData)

            // 3. Add summary statistics
//            addSummaryStatistics(document, projectData, timesheetEntries)

            // 4. Add charts and visualizations
            addProjectAnalysis(document, timesheetEntries)

            // 5. Add detailed timesheet entries
            addTimesheetEntries(document, timesheetEntries)

            // 6. Add footer
            addReportFooter(document)

        } finally {
            document.close()
        }

        return outputStream.toByteArray()
    }

    /**
     * Add report header to the document
     */
    private fun addReportHeader(document: Document) {
        val title = Paragraph("Project Timesheet Report")
            .setFontSize(24f)
            .simulateBold()
            .setTextAlignment(TextAlignment.CENTER)
        document.add(title)
        val date = System.now().format(DateTimeComponents.Format {
            year()
            char('-')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char('-')
            dayOfMonth()
        })
        val subtitle = Paragraph("Generated on $date)}")
            .setFontSize(12f)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(DeviceRgb(128, 128, 128))
            .setMarginBottom(20f)
        document.add(subtitle)

        document.add(LineSeparator(TabStop(1f).tabLeader).setMarginBottom(20f))
    }

    /**
     * Add project information section
     */
    private fun addProjectInformation(document: Document, projectData: ProjectData) {
        val sectionTitle = Paragraph("Project Information")
            .setFontSize(16f)
            .simulateBold()
            .setMarginBottom(10f)
        document.add(sectionTitle)

        val table = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
            .useAllAvailableWidth()
            .setBorder(Border.NO_BORDER)

        // First column
        table.addCell(createInfoCell("Project Name:", projectData.name))  //todo:
        table.addCell(createInfoCell("Project ID:", projectData.id)) //todo: create
        table.addCell(createInfoCell("Project Manager:", projectData.projectManager))

        // Second column
        table.addCell(createInfoCell("Start Date:", projectData.startDate))
        table.addCell(createInfoCell("End Date:", projectData.endDate))
        table.addCell(createInfoCell("Status:", projectData.status))

        document.add(table)
        document.add(LineSeparator(TabStop(0f).tabLeader).setMarginTop(15f).setMarginBottom(20f))
    }

    /**
     * Helper method to create cells for the project info table
     */
    private fun createInfoCell(label: String, value: String): Cell {
        val cell = Cell()
            .setBorder(Border.NO_BORDER)
            .setPadding(5f)

        val paragraph = Paragraph()
        paragraph.add(Text(label).simulateBold())
        paragraph.add(Text(" $value"))

        cell.add(paragraph)
        return cell
    }

    /**
     * Add summary statistics section
     */
    private fun addSummaryStatistics(
        document: Document,
        projectData: ProjectData,
        timesheetEntries: List<MasterExcelEntry>
    ) {
        val sectionTitle = Paragraph("Summary Statistics")
            .setFontSize(16f)
            .simulateBold()
            .setMarginBottom(10f)
        document.add(sectionTitle)

        val table = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
            .useAllAvailableWidth()
            .setMarginBottom(15f)

        // Calculate team members count
        val teamMembersCount = timesheetEntries
            .map { it.userCode }.distinct().size

        // Total Hours Box
        table.addCell(
            createStatsBox(
                "Total Hours Logged",
                projectData.totalHoursLogged.toString(),
                "of ${projectData.totalBudgetHours} budgeted hours",
                DeviceRgb(235, 245, 255) // blue-50
            )
        )

        // Project Completion Box
        val completionBox = createStatsBox(
            "Project Completion",
            "${projectData.completionPercentage}%",
            "",
            DeviceRgb(240, 255, 244) // green-50
        )
        // Add progress bar
        val progressBar = createProgressBar(projectData.completionPercentage.toInt())
        completionBox.add(progressBar)
        table.addCell(completionBox)

        // Team Members Box
        table.addCell(
            createStatsBox(
                "Total Team Members",
                teamMembersCount.toString(),
                "",
                DeviceRgb(250, 245, 255) // purple-50
            )
        )

        // Total Entries Box
        table.addCell(
            createStatsBox(
                "Total Entries",
                timesheetEntries.size.toString(),
                "",
                DeviceRgb(255, 251, 235) // yellow-50
            )
        )

        document.add(table)
        document.add(LineSeparator(TabStop(1f).tabLeader).setMarginBottom(20f))
    }

    /**
     * Helper method to create a stats box with title, value and subtitle
     */
    private fun createStatsBox(title: String, value: String, subtitle: String, backgroundColor: DeviceRgb): Cell {
        val cell = Cell()
            .setPadding(10f)
            .setBackgroundColor(backgroundColor)
            .setBorderRadius(BorderRadius(5f))
            .setMargin(5f)

        val titleText = Paragraph(title)
            .setFontSize(10f)
            .setFontColor(DeviceRgb(75, 85, 99)) // gray-600

        val valueText = Paragraph(value)
            .setFontSize(18f)
            .simulateBold()

        cell.add(titleText)
        cell.add(valueText)

        if (subtitle.isNotEmpty()) {
            val subtitleText = Paragraph(subtitle)
                .setFontSize(10f)
                .setFontColor(DeviceRgb(107, 114, 128)) // gray-500
            cell.add(subtitleText)
        }

        return cell
    }

    /**
     * Create a simple progress bar for the completion percentage
     */
    private fun createProgressBar(percentage: Int): Div {
        val barContainer = Div()
            .setWidth(UnitValue.createPercentValue(100f))
            .setHeight(8f)
            .setBackgroundColor(DeviceRgb(229, 231, 235)) // gray-200
            .setBorderRadius(BorderRadius(4f))
            .setMarginTop(5f)

        // Progress indicator
        val progress = Div()
            .setWidth(UnitValue.createPercentValue(percentage.toFloat()))
            .setHeight(8f)
            .setBackgroundColor(DeviceRgb(22, 163, 74)) // green-600
            .setBorderRadius(BorderRadius(4f))

        barContainer.add(progress)
        return barContainer
    }

    /**
     * Add project analysis section with charts
     */
    fun addProjectAnalysis(document: Document, timesheetEntries: List<MasterExcelEntry>) {
//        val sectionTitle = Paragraph("Project Analysis")
//            .setFontSize(16f)
//            .simulateBold()
//            .setMarginBottom(10f)
//        document.add(sectionTitle)
//
//        // Group entries by task and calculate hours
//        val taskData = timesheetEntries
//            .groupBy { it.taskName }
//            .mapValues { (_, entries) -> entries.sumOf { it.hoursWorked.toDoubleOrNull() ?: 0.0 } }
//        val totalTaskHours = taskData.values.sum()
//
//        // Group entries by month and calculate hours
//        val monthlyData = timesheetEntries
//            .filter { it.dateOfWork != null }
//            .mapNotNull { entry ->
//                try {
//                    val dateEntered = LocalDate.parse(entry.dateOfWork.toString())
//                    "${dateEntered.year}, ${dateEntered.month.value}" to (entry.hoursWorked.toDoubleOrNull() ?: 0.0)
//                } catch (e: Exception) {
//                    null // Skip entries with invalid dates
//                }
//            }
//            .groupBy { "${it.first}${it.second}" }
//            .mapValues { (_, pairs) -> pairs.sumOf { it.second } }
//            .toSortedMap()
//        // Create a table for the charts (2 columns)
//        val chartsTable = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
//            .useAllAvailableWidth()
//            .setMarginBottom(15f)
//
//        // Task distribution chart
//        val taskChartTitle = Paragraph("Hours by Task Type")
//            .setFontSize(14f)
//            .simulateBold()
//            .setTextAlignment(TextAlignment.CENTER)
//
//        val taskPieChart = letsPlot(taskData) + ggsize(500, 200)
//        taskPieChart + geomPie(
//            stat = StatOptions(StatKind.IDENTITY),
//            size = 20,
//            stroke = 1,
//            color = "white",
//            hole = 0.5
//        ) { x = "task"; y = "value" } + theme(line=elementBlank(), axis=elementBlank()) + scaleFillBrewer(palette = "Set1")
//
//        val taskChart = DefaultPlotPanelBatik(processedSpec = MonolithicCommon.processRawSpecs(taskPieChart.toSpec(), false),
//            preserveAspectRatio = true,
//            preferredSizeFromPlot = false,
//            repaintDelay = 10
//        ) { messages ->
//            for (message in messages) {
//                println("[Example App] $message")
//            }
//        }
//        val createTaskChart = createChartImage(taskChart, 500, 200)
//        val taskChartCell = Cell()
////            .add(taskChartTitle)
//            .add(createTaskChart)
//            .setTextAlignment(TextAlignment.CENTER)
//            .setPadding(20f)
//            .setBackgroundColor(DeviceRgb(249, 250, 251)) // gray-50
//
////        chartsTable.addCell(employeeChartCell)
//        chartsTable.addCell(taskChartCell)
//        document.add(chartsTable)
//
//        // Monthly hours chart (full width)
//        val monthlyChartTitle = Paragraph("Hours by Month")
//            .setFontSize(14f)
//            .simulateBold()
//            .setTextAlignment(TextAlignment.CENTER)
//
//        val formattedMonthlyData = monthlyData.entries.associate { (month, hours) ->
//            month.format(DateTimeFormatter.ofPattern("MMM yyyy")) to hours
//        }
//
//        val monthlyBarChart = createBarChart(
//            "Monthly Hours",
//            formattedMonthlyData,
//            "Month",
//            "Hours"
//        )
//
//        val createMonthlyChart = createChartImage(monthlyBarChart, 500, 200)
//        val monthlyChartDiv = Div()
//            .add(monthlyChartTitle)
//            .add(createMonthlyChart)
//            .setTextAlignment(TextAlignment.CENTER)
//            .setPadding(10f)
//            .setBackgroundColor(DeviceRgb(249, 250, 251)) // gray-50
//
//        document.add(monthlyChartDiv)
//        document.add(LineSeparator(TabStop(1f).tabLeader).setMarginTop(15f).setMarginBottom(20f))
    }

    /**
     * Create a pie chart using JFreeChart
     */
    private fun createPieChart(
        title: String,
        data: Map<String, Double>,
        valueName: String
    ): JFreeChart {
        val dataset = DefaultPieDataset<String>()

        // Add data to dataset
        data.forEach { (key, value) ->
            dataset.setValue(key, value)
        }

        // Create the chart
        val chart = ChartFactory.createPieChart(
            title,
            dataset,
            true,  // include legend
            true,  // tooltips
            false  // URLs
        )

        // Customize the chart
        val plot = chart.plot as PiePlot<*>

        // Set colors for sections
        data.keys.forEachIndexed { index, key ->
            val color = CHART_COLORS[index % CHART_COLORS.size]
            plot.setSectionPaint(key, color)
        }

        // Style chart
        chart.backgroundPaint = Color(249, 250, 251) // gray-50

        return chart
    }

    /**
     * Create a bar chart using JFreeChart
     */
    fun createBarChart(data: Map<String, List<*>>): Plot {
        return letsPlot(data) +
                geomBar(
                    stat = StatOptions(StatKind.IDENTITY),
                    alpha = 0.8,
                    fill = "green", // Map the fill color to the category
                    tooltips = layerTooltips()
                        .line("category")
                        .line("Value: @value")
                ) {
                    x = "category"
                    y = "value"
                } +
                ggtitle("Category Values") +
                theme(
                    line = elementBlank(),
//                    axis = elementBlank(),
//                    legend = elementBlank(),
//                    plotBackground = elementBlank(),
//                    panelBackground = elementBlank(),
//                    panelBorder = elementBlank(),
//                    panelGrid = elementBlank(),
                    plotMargin = UnitValue.createPercentValue(0f)
                ) +
//                    axisTitleX = "Category",
//                    axisTitleY = "Value"
//                ) +
                scaleFillBrewer(palette = "Set2") +
                coordFlip() // Optional: flip coordinates for horizontal bars
    }

    /**
     * Convert a JFreeChart to an iText Image
     */
    private fun createChartImage(chart: JPanel, width: Int, height: Int): Image {
        val bufferedImage = chart.createBufferedImage(width, height)
        val baos = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", baos)

        return Image(ImageDataFactory.create(baos.toByteArray()))
            .setHorizontalAlignment(HorizontalAlignment.CENTER)
    }

    /**
     * Add detailed timesheet entries grouped by month
     */
    private fun addTimesheetEntries(document: Document, timesheetEntries: List<MasterExcelEntry>) {
        val sectionTitle = Paragraph("Timesheet Entries")
            .setFontSize(16f)
            .simulateBold()
            .setMarginBottom(10f)
        document.add(sectionTitle)

        // Group entries by month - with proper error handling
        val entriesByMonth = timesheetEntries
            .groupBy { it.dateOfWork }


        // For each month
        entriesByMonth.toSortedMap(compareByDescending { it?.toEpochDays() }).forEach { (date, entries) ->
            // Format the month heading with error handling
            val displayMonth = try {
                val yearMonth = "${date?.month?.name} ${date?.year}"
                yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            } catch (e: Exception) {
                "Unknown Month"
            }

            // Calculate monthly total with null safety
            val monthlyTotal = entries.sumOf { entry ->
                try {
                    entry.hoursWorked.toDoubleOrNull() ?: 0.0
                } catch (e: Exception) {
                    0.0
                }
            }
            entriesByMonth

            // Add month heading with total
            val monthHeading = Div()
                .setMarginBottom(5f)

            val monthTitle = Paragraph(displayMonth)
                .setFontSize(14f)
                .simulateBold()

            val totalText = Paragraph("${String.format("%.1f", monthlyTotal)} hours")
                .setFontSize(12f)
                .setFontColor(DeviceRgb(75, 85, 99)) // gray-600
                .setTextAlignment(TextAlignment.RIGHT)

            val headingTable = Table(UnitValue.createPercentArray(floatArrayOf(70f, 30f)))
                .useAllAvailableWidth()
                .setBorder(Border.NO_BORDER)

            headingTable.addCell(Cell().add(monthTitle).setBorder(Border.NO_BORDER))
            headingTable.addCell(
                Cell().add(totalText).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT)
            )

            monthHeading.add(headingTable)
            document.add(monthHeading)

//            // Create entries table
//            val entriesTable = Table(UnitValue.createPercentArray(floatArrayOf(15f, 20f, 20f, 10f, 35f)))
//                .useAllAvailableWidth()
//                .setMarginBottom(15f)
//
//            // Add header row
//            HEADER_CELLS.forEach { headerText ->
//                entriesTable.addHeaderCell(
//                    Cell()
//                        .add(Paragraph(headerText).simulateBold())
//                        .setBackgroundColor(DeviceRgb(249, 250, 251)) // gray-50
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setPadding(5f)
//                )
//            }
//
//            // Sort entries by date with error handling
//            val sortedEntries = try {
//                entries.sortedBy { it.dateOfWork }
//            } catch (e: Exception) {
//                entries // Fall back to unsorted if sorting fails
//            }
//
//            // Add entry rows with null safety
//            sortedEntries.forEach { entry ->
//                entriesTable.addCell(Cell().add(Paragraph(entry.dateOfWork!!.toString() ?: "")).setPadding(5f))
//                entriesTable.addCell(Cell().add(Paragraph(entry.hoursWorked ?: "")).setPadding(5f))
//                entriesTable.addCell(Cell().add(Paragraph(entry.taskName ?: "")).setPadding(5f))
//
//                // Safely format hours
//                val hours = try {
//                    String.format("%.1f", entry.hoursWorked.toDoubleOrNull() ?: 0.0)
//                } catch (e: Exception) {
//                    "0.0"
//                }
//
//                entriesTable.addCell(
//                    Cell().add(Paragraph(hours))
//                        .setTextAlignment(TextAlignment.RIGHT).setPadding(5f)
//                )
//                entriesTable.addCell(Cell().add(Paragraph(entry.projectJobId)).setPadding(5f))
//            }
//
//            document.add(entriesTable)
        }
    }

    /**
     * Add report footer
     */
    private fun addReportFooter(document: Document) {
        document.add(LineSeparator(TabStop(1f).tabLeader).setMarginBottom(10f))

        val footer = Paragraph("Generated from Project Management System")
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(DeviceRgb(107, 114, 128)) // gray-500

        val copyright = Paragraph("© ${LocalDate(2025, 3, 10)} Your Company")
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(DeviceRgb(107, 114, 128)) // gray-500

        document.add(footer)
        document.add(copyright)
    }


    fun extractProjectDataFromEntries(
        entries: List<MasterExcelEntry>
    ): ProjectData {
        val startDate = entries.map { it.dateOfWork }.minBy { it!!.toEpochDays() }!!
        val endDate = entries.map { it.dateOfWork }.maxBy { it!!.toEpochDays() }!!
        val totalBudgetHours = 1000.0
        val loggedHours = entries.sumOf { it.hoursWorked.toDoubleOrNull() ?: 0.0 }
        return ProjectData(
            id = entries.last().projectJobId,
            name = entries.last().jobName,
            projectManager = entries.last().foremanName,
            startDate = startDate.format(LocalDate.Format {
                monthName(MonthNames.ENGLISH_FULL)
                char(' ')
                dayOfMonth()
                char(',')
                year()
            }),
            endDate = endDate.format(LocalDate.Format {
                monthName(MonthNames.ENGLISH_FULL)
                char(' ')
                dayOfMonth()
                char(',')
                year()
            }),
            status = "ARCHIVED",
            completionPercentage = (loggedHours / totalBudgetHours) * 100,
            totalBudgetHours = totalBudgetHours,
            totalHoursLogged = loggedHours,

            )
    }

    fun getEntriesFromExcel(file: File): MutableList<MasterExcelEntry> {
        val entryList: MutableList<MasterExcelEntry> = mutableListOf()
        val entries = HashMap<Row, MutableList<String>>()
        WorkbookFactory.create(file.inputStream()).getSheetAt(0).spliterator().forEachRemaining { row ->
            val rowCells: MutableList<String> = mutableListOf()

            row.forEach { cell ->
                rowCells.add(cell.toString())
            }

            if (rowCells.size == 12) {
                if (rowCells[0] == "UserCode") {
                    return@forEachRemaining
                }
                val userCode = rowCells[0]
                val taskName = rowCells[1]
                val hoursWorked = rowCells[2]
                val overTime = rowCells[3]
                val dateOfWork = parseDateFromDateOfWork(rowCells[4])
                val projectJobId = rowCells[5]
                val shiftType = rowCells[6]
                val foremanName = rowCells[7]
                val isVerifiedForeman = rowCells[8]
                val jobName = rowCells[9]
                val estimatedHours = rowCells[10]
                val taskId = rowCells[11]
                val entry = MasterExcelEntry(
                    userCode = userCode,
                    taskName = taskName,
                    taskId = taskId,
                    hoursWorked = hoursWorked,
                    overTime = overTime,
                    dateOfWork = dateOfWork,
                    projectJobId = projectJobId,
                    shiftType = shiftType,
                    foremanName = foremanName,
                    jobName = jobName,
                    estimatedHours = estimatedHours,
                    isVerifiedForeman = isVerifiedForeman
                )
                entryList.add(entry)
            }
            entries.put(row, rowCells)
        }



        return entryList
    }

    companion object {
        private val HEADER_CELLS = listOf("Date", "Employee", "Task", "Hours", "Notes")

        fun parseDateFromDateOfWork(dateString: String): LocalDate {
            return try {
                val dateFormat = LocalDate.Format {
                    dayOfMonth()
                    char('-')
                    monthName(MonthNames.ENGLISH_ABBREVIATED)
                    char('-')
                    year()
                }

                dateFormat.parse(dateString)

            } catch (e: Exception) {
                LocalDate(2020, 3, 10)
            }
        }


    }

    /**
     * Project data class
     */
    data class ProjectData(
        val id: String,
        val name: String,
        val projectManager: String,
        val startDate: String,
        val endDate: String,
        val status: String,
        val completionPercentage: Double,
        val totalBudgetHours: Double,
        val totalHoursLogged: Double
    )
}

private fun JPanel.createBufferedImage(width: Int, height: kotlin.Int): BufferedImage {
    this.size = this.preferredSize
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics = bufferedImage.graphics
    this.paint(graphics)
    graphics.dispose()

    return bufferedImage
}
