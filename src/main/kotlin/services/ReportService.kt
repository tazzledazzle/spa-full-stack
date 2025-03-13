//package com.northshore.services
//
//import com.itextpdf.kernel.colors.ColorConstants
//import com.itextpdf.kernel.pdf.PdfDocument
//import com.itextpdf.kernel.pdf.PdfWriter
//import com.itextpdf.layout.Document
//import com.itextpdf.layout.element.Cell
//import com.itextpdf.layout.element.Paragraph
//import com.itextpdf.layout.element.Table
//import com.itextpdf.layout.properties.TextAlignment
//import com.itextpdf.layout.properties.UnitValue
//import com.northshore.models.TimesheetEntry
//import org.apache.poi.ss.usermodel.WorkbookFactory
//import org.springframework.web.multipart.MultipartFile
//import java.io.ByteArrayOutputStream
//import java.time.LocalDate
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.util.*
//
//class PdfReportService(
//    private val projectRepository: ProjectRepository,
//    private val taskRepository: TaskRepository,
//    private val timesheetEntryRepository: TimesheetEntryRepository
//) {
//
//    /**
//     * Generate a PDF report from an Excel file
//     * @param excelFile The Excel file to process
//     * @param reportTitle Optional report title
//     * @return ByteArray containing the PDF document
//     */
//    fun generatePdfFromExcel(excelFile: MultipartFile, reportTitle: String?): ByteArray {
//        val workbook = WorkbookFactory.create(excelFile.inputStream)
//        val sheet = workbook.getSheetAt(0)
//
//        // Extract data from Excel
//        val employeeName = sheet.getRow(2).getCell(1)?.stringCellValue ?: "Not Specified"
//        val projectName = sheet.getRow(3).getCell(1)?.stringCellValue ?: "Not Specified"
//
//        val entries = mutableListOf<TimesheetEntry>()
//
//        // Start from row 5 (index 4) and collect timesheet entries
//        var rowIndex = 4
//        while (rowIndex < 19) {
//            val row = sheet.getRow(rowIndex)
//            if (row != null) {
//                val dateValue = row.getCell(0)?.stringCellValue?.trim()
//                val taskValue = row.getCell(2)?.stringCellValue?.trim()
//                val hoursValue = row.getCell(3)?.numericCellValue
//                val notesValue = row.getCell(4)?.stringCellValue?.trim()
//
//                if (!taskValue.isNullOrEmpty() && hoursValue != null) {
//                    entries.add(
//                        TimesheetEntry(
//                            date = dateValue ?: "",
//                            task = taskValue,
//                            hours = hoursValue,
//                            notes = notesValue ?: ""
//                        )
//                    )
//                }
//            }
//            rowIndex++
//        }
//
//        // Calculate total hours
//        val totalHours = entries.sumOf { it.hours }
//
//        // Generate PDF
//        return generateTimesheetPdf(
//            employeeName = employeeName,
//            projectName = projectName,
//            entries = entries,
//            totalHours = totalHours,
//            reportTitle = reportTitle ?: "$projectName Timesheet Report"
//        )
//    }
//
//    /**
//     * Generate a PDF report for a project based on database records
//     * @param projectId The ID of the project
//     * @return ByteArray containing the PDF document
//     */
//    fun generateProjectReport(projectId: Long): ByteArray {
//        val project = projectRepository.findById(projectId)
//            .orElseThrow { IllegalArgumentException("Project not found with ID: $projectId") }
//
//        val tasks = taskRepository.findByProjectId(projectId)
//        val timesheetEntries = timesheetEntryRepository.findByTaskProjectId(projectId)
//
//        return generateProjectPdf(project, tasks, timesheetEntries)
//    }
//
//    /**
//     * Generate a PDF report for a specific user's timesheets
//     * @param employeeName The name of the employee
//     * @param startDate Optional start date for filtering
//     * @param endDate Optional end date for filtering
//     * @return ByteArray containing the PDF document
//     */
//    fun generateEmployeeReport(employeeName: String, startDate: LocalDate?, endDate: LocalDate?): ByteArray {
//        val entries = if (startDate != null && endDate != null) {
//            timesheetEntryRepository.findByEmployeeNameAndWorkDateBetween(employeeName, startDate, endDate)
//        } else {
//            timesheetEntryRepository.findByEmployeeName(employeeName)
//        }
//
//        return generateEmployeePdf(employeeName, entries, startDate, endDate)
//    }
//
//    // Private helper methods
//
//    private fun generateTimesheetPdf(
//        employeeName: String,
//        projectName: String,
//        entries: List<TimesheetEntry>,
//        totalHours: Double,
//        reportTitle: String
//    ): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        val pdfWriter = PdfWriter(outputStream)
//        val pdfDocument = PdfDocument(pdfWriter)
//        val document = Document(pdfDocument)
//
//        try {
//            // Add title
//            val title = Paragraph(reportTitle)
//                .setFontSize(18f)
//                .setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginBottom(20f)
//            document.add(title)
//
//            // Add metadata
//            document.add(Paragraph("Employee: $employeeName").setMarginBottom(5f))
//            document.add(Paragraph("Project: $projectName").setMarginBottom(5f))
//            document.add(Paragraph("Report Date: ${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}").setMarginBottom(15f))
//
//            // Create table
//            val table = Table(UnitValue.createPercentArray(floatArrayOf(15f, 40f, 15f, 30f)))
//                .useAllAvailableWidth()
//                .setMarginBottom(15f)
//
//            // Add header row
//            listOf("Date", "Task", "Hours", "Notes").forEach { headerText ->
//                table.addHeaderCell(
//                    Cell().add(Paragraph(headerText).setBold())
//                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                        .setTextAlignment(TextAlignment.CENTER)
//                )
//            }
//
//            // Add data rows
//            entries.forEach { entry ->
//                table.addCell(Cell().add(Paragraph(entry.date)).setTextAlignment(TextAlignment.CENTER))
//                table.addCell(Cell().add(Paragraph(entry.task)))
//                table.addCell(Cell().add(Paragraph(String.format("%.1f", entry.hours))).setTextAlignment(TextAlignment.RIGHT))
//                table.addCell(Cell().add(Paragraph(entry.notes)))
//            }
//
//            document.add(table)
//
//            // Add total hours
//            document.add(
//                Paragraph("Total Hours: ${String.format("%.1f", totalHours)}")
//                    .setBold()
//                    .setTextAlignment(TextAlignment.RIGHT)
//            )
//
//            // Add footer
//            val footer = Paragraph("Generated from $projectName timesheet on ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
//                .setFontSize(9f)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginTop(30f)
//            document.add(footer)
//
//        } finally {
//            document.close()
//        }
//
//        return outputStream.toByteArray()
//    }
//
//    private fun generateProjectPdf(
//        project: ProjectDto,
//        tasks: List<TaskDto>,
//        timesheetEntries: List<TimesheetEntryDto>
//    ): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        val pdfWriter = PdfWriter(outputStream)
//        val pdfDocument = PdfDocument(pdfWriter)
//        val document = Document(pdfDocument)
//
//        try {
//            // Add title
//            val title = Paragraph("${project.name} - Project Report")
//                .setFontSize(18f)
//                .setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginBottom(20f)
//            document.add(title)
//
//            // Add project metadata
//            document.add(Paragraph("Project Manager: ${project.projectManagerName ?: "Not Assigned"}").setMarginBottom(5f))
//            document.add(Paragraph("Start Date: ${project.startDate?.format(DateTimeFormatter.ISO_DATE) ?: "Not Set"}").setMarginBottom(5f))
//            document.add(Paragraph("End Date: ${project.endDate?.format(DateTimeFormatter.ISO_DATE) ?: "Not Set"}").setMarginBottom(5f))
//            document.add(Paragraph("Report Generated: ${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}").setMarginBottom(15f))
//
//            // Task summary section
//            document.add(Paragraph("Task Summary").setFontSize(14f).setBold().setMarginBottom(10f))
//
//            val taskTable = Table(UnitValue.createPercentArray(floatArrayOf(40f, 20f, 40f)))
//                .useAllAvailableWidth()
//                .setMarginBottom(15f)
//
//            // Add task header row
//            listOf("Task Name", "Estimated Hours", "Description").forEach { headerText ->
//                taskTable.addHeaderCell(
//                    Cell().add(Paragraph(headerText).setBold())
//                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                        .setTextAlignment(TextAlignment.CENTER)
//                )
//            }
//
//            // Add task data rows
//            tasks.forEach { task ->
//                taskTable.addCell(Cell().add(Paragraph(task.name)))
//                taskTable.addCell(Cell().add(Paragraph(task.estimatedHours?.toString() ?: "N/A")).setTextAlignment(TextAlignment.CENTER))
//                taskTable.addCell(Cell().add(Paragraph(task.description ?: "")))
//            }
//
//            document.add(taskTable)
//
//            // Timesheet summary by employee
//            document.add(Paragraph("Timesheet Summary by Employee").setFontSize(14f).setBold().setMarginBottom(10f))
//
//            // Group entries by employee
//            val entriesByEmployee = timesheetEntries.groupBy { it.employeeName }
//
//            val employeeTable = Table(UnitValue.createPercentArray(floatArrayOf(40f, 20f, 20f, 20f)))
//                .useAllAvailableWidth()
//                .setMarginBottom(15f)
//
//            // Add employee summary header row
//            listOf("Employee", "Total Hours", "Entries", "Last Submission").forEach { headerText ->
//                employeeTable.addHeaderCell(
//                    Cell().add(Paragraph(headerText).setBold())
//                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                        .setTextAlignment(TextAlignment.CENTER)
//                )
//            }
//
//            // Add employee summary data rows
//            entriesByEmployee.forEach { (employee, entries) ->
//                employeeTable.addCell(Cell().add(Paragraph(employee)))
//
//                val totalHours = entries.sumOf { it.hoursWorked }
//                employeeTable.addCell(Cell().add(Paragraph(String.format("%.1f", totalHours))).setTextAlignment(TextAlignment.RIGHT))
//
//                employeeTable.addCell(Cell().add(Paragraph(entries.size.toString())).setTextAlignment(TextAlignment.CENTER))
//
//                val lastSubmission = entries.maxByOrNull { it.submittedAt ?: LocalDateTime.MIN }?.submittedAt
//                employeeTable.addCell(Cell().add(
//                    Paragraph(lastSubmission?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "N/A")
//                ).setTextAlignment(TextAlignment.CENTER))
//            }
//
//            document.add(employeeTable)
//
//            // Recent entries
//            document.add(Paragraph("Recent Timesheet Entries").setFontSize(14f).setBold().setMarginBottom(10f))
//
//            val recentEntries = timesheetEntries
//                .sortedByDescending { it.submittedAt }
//                .take(10)
//
//            val recentTable = Table(UnitValue.createPercentArray(floatArrayOf(15f, 20f, 25f, 10f, 30f)))
//                .useAllAvailableWidth()
//                .setMarginBottom(15f)
//
//            // Add recent entries header row
//            listOf("Date", "Employee", "Task", "Hours", "Notes").forEach { headerText ->
//                recentTable.addHeaderCell(
//                    Cell().add(Paragraph(headerText).setBold())
//                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                        .setTextAlignment(TextAlignment.CENTER)
//                )
//            }
//
//            // Add recent entries data rows
//            recentEntries.forEach { entry ->
//                recentTable.addCell(Cell().add(Paragraph(entry.workDate.format(DateTimeFormatter.ISO_DATE))).setTextAlignment(TextAlignment.CENTER))
//                recentTable.addCell(Cell().add(Paragraph(entry.employeeName)))
//                recentTable.addCell(Cell().add(Paragraph(entry.taskName)))
//                recentTable.addCell(Cell().add(Paragraph(String.format("%.1f", entry.hoursWorked))).setTextAlignment(TextAlignment.RIGHT))
//                recentTable.addCell(Cell().add(Paragraph(entry.notes ?: "")))
//            }
//
//            document.add(recentTable)
//
//            // Project totals
//            val totalProjectHours = timesheetEntries.sumOf { it.hoursWorked }
//            document.add(Paragraph("Project Summary:").setFontSize(14f).setBold().setMarginBottom(10f))
//            document.add(Paragraph("Total Hours Logged: ${String.format("%.1f", totalProjectHours)}").setMarginBottom(5f))
//            document.add(Paragraph("Number of Entries: ${timesheetEntries.size}").setMarginBottom(5f))
//            document.add(Paragraph("Number of Contributors: ${entriesByEmployee.size}").setMarginBottom(15f))
//
//            // Add footer
//            val footer = Paragraph("Generated from Project Management System on ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
//                .setFontSize(9f)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginTop(30f)
//            document.add(footer)
//
//        } finally {
//            document.close()
//        }
//
//        return outputStream.toByteArray()
//    }
//
//    private fun generateEmployeePdf(
//        employeeName: String,
//        entries: List<TimesheetEntry>,
//        startDate: LocalDate?,
//        endDate: LocalDate?
//    ): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        val pdfWriter = PdfWriter(outputStream)
//        val pdfDocument = PdfDocument(pdfWriter)
//        val document = Document(pdfDocument)
//
//        try {
//            // Add title
//            val dateRange = if (startDate != null && endDate != null) {
//                " (${startDate.format(DateTimeFormatter.ISO_DATE)} to ${endDate.format(DateTimeFormatter.ISO_DATE)})"
//            } else {
//                ""
//            }
//
//            val title = Paragraph("Timesheet Report - $employeeName$dateRange")
//                .setFontSize(18f)
//                .setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginBottom(20f)
//            document.add(title)
//
//            // Add metadata
//            document.add(Paragraph("Employee: $employeeName").setMarginBottom(5f))
//            document.add(Paragraph("Report Generated: ${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}").setMarginBottom(15f))
//
//            // Group entries by project
////            val entriesByProject = entries.groupBy { it.projectName ?: "Unknown Project" }
//            val entriesByProject = entries.groupBy { (k,v) ->  }
//            // For each project, create a section
//            entriesByProject.forEach { (projectName, projectEntries) ->
//                document.add(
//                    Paragraph("Project: $projectName")
//                        .setFontSize(14f)
//                        .setBold()
//                        .setMarginBottom(10f)
//                )
//
//                // Create table for this project
//                val table = Table(UnitValue.createPercentArray(floatArrayOf(15f, 30f, 10f, 45f)))
//                    .useAllAvailableWidth()
//                    .setMarginBottom(15f)
//
//                // Add header row
//                listOf("Date", "Task", "Hours", "Notes").forEach { headerText ->
//                    table.addHeaderCell(
//                        Cell().add(Paragraph(headerText).setBold())
//                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                            .setTextAlignment(TextAlignment.CENTER)
//                    )
//                }
//
//                // Add data rows for this project
//                projectEntries
//                    .sortedBy { it.workDate }
//                    .forEach { entry ->
//                        table.addCell(Cell().add(Paragraph(entry.workDate.format(DateTimeFormatter.ISO_DATE))).setTextAlignment(TextAlignment.CENTER))
//                        table.addCell(Cell().add(Paragraph(entry.taskName)))
//                        table.addCell(Cell().add(Paragraph(String.format("%.1f", entry.hoursWorked))).setTextAlignment(TextAlignment.RIGHT))
//                        table.addCell(Cell().add(Paragraph(entry.notes ?: "")))
//                    }
//
//                document.add(table)
//
//                // Add project total
//                val projectTotal = projectEntries.sumOf { it.hoursWorked }
//                document.add(
//                    Paragraph("Total Hours for $projectName: ${String.format("%.1f", projectTotal)}")
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setMarginBottom(20f)
//                )
//            }
//
//            // Add grand total
//            val totalHours = entries.sumOf { it.hoursWorked }
//            document.add(
//                Paragraph("Grand Total Hours: ${String.format("%.1f", totalHours)}")
//                    .setBold()
//                    .setTextAlignment(TextAlignment.RIGHT)
//                    .setMarginBottom(20f)
//            )
//
//            // Summary statistics
//            document.add(Paragraph("Summary Statistics").setFontSize(14f).setBold().setMarginBottom(10f))
//
//            val statsTable = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
//                .useAllAvailableWidth()
//                .setMarginBottom(15f)
//
//            statsTable.addCell(Cell().add(Paragraph("Total Hours Logged")).setBold())
//            statsTable.addCell(Cell().add(Paragraph(String.format("%.1f", totalHours))))
//
//            statsTable.addCell(Cell().add(Paragraph("Number of Entries")).setBold())
//            statsTable.addCell(Cell().add(Paragraph(entries.size.toString())))
//
//            statsTable.addCell(Cell().add(Paragraph("Number of Projects")).setBold())
//            statsTable.addCell(Cell().add(Paragraph(entriesByProject.size.toString())))
//
//            statsTable.addCell(Cell().add(Paragraph("Date Range")).setBold())
//            val minDate = entries.minOfOrNull { it.workDate }?.format(DateTimeFormatter.ISO_DATE) ?: "N/A"
//            val maxDate = entries.maxOfOrNull { it.workDate }?.format(DateTimeFormatter.ISO_DATE) ?: "N/A"
//            statsTable.addCell(Cell().add(Paragraph("$minDate to $maxDate")))
//
//            document.add(statsTable)
//
//            // Add footer
//            val footer = Paragraph("Generated from Project Management System on ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
//                .setFontSize(9f)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginTop(30f)
//            document.add(footer)
//
//        } finally {
//            document.close()
//        }
//
//        return outputStream.toByteArray()
//    }
//
//}