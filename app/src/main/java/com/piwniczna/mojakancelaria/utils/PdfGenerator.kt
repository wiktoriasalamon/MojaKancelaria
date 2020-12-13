package com.piwniczna.mojakancelaria.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.itextpdf.html2pdf.HtmlConverter
import java.io.File
import java.io.FileOutputStream


class PdfGenerator {

    companion object {

        fun generatePdfFromHTML(context: Context, html: String) : Uri {
            val fname = "report.pdf"

            val fOut: FileOutputStream = FileOutputStream(File(context.getExternalFilesDir(null),fname)  )
            HtmlConverter.convertToPdf(html, fOut)
            val a = context.fileList()


            val uri = Uri.fromFile(File(context.getExternalFilesDir(null), fname))

            return uri
        }



    }
}

