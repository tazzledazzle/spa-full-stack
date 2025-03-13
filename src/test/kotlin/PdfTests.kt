package com.northshore

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import io.kotest.core.spec.style.FunSpec

class PdfTests: FunSpec({
    test("hello world pdf") {
        val pdf = PdfDocument(PdfWriter("hello.pdf", WriterProperties().setPdfVersion(PdfVersion.PDF_2_0)))
        val document = Document(pdf, PageSize.A4)
        document.add(Paragraph("Hello, World!"))
        document.close()
    }

    test("Extract From Excel") {

    }
})