package com.piwniczna.mojakancelaria.utils

import android.content.Context
import android.net.Uri
import com.itextpdf.html2pdf.ConverterProperties
import com.itextpdf.html2pdf.HtmlConverter
import java.io.File
import java.io.FileOutputStream


class PdfGenerator {

    companion object {

        fun generatePdfFromHTML(context: Context, html: String) : Uri {
            val fname = "report.pdf"


            val fOut = FileOutputStream(File(context.getExternalFilesDir(null),fname)  )

            var properties = ConverterProperties()
            properties.charset = "utf-8"
            HtmlConverter.convertToPdf(html, fOut, properties )


            val uri = Uri.fromFile(File(context.getExternalFilesDir(null), fname))

            return uri
        }



    }
}

