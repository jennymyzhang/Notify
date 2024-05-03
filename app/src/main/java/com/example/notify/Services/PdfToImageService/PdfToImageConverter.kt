package com.example.notify.Services.PdfToImageService

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.IOException

class PdfToImageConverter {
    private val defaultWidthPixels = 1080
    private val defaultHeightPixels = 1920

    fun convertPdfToImages(pdfUri: Uri, context: Context): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        val fileDescriptor = context.contentResolver.openFileDescriptor(pdfUri, "r")
        fileDescriptor?.let {
            val pdfRenderer = PdfRenderer(it)
            val pageCount = pdfRenderer.pageCount
            for (i in 0 until pageCount) {
                val page = pdfRenderer.openPage(i)

                val pageWidth = page.width
                val pageHeight = page.height
                val scale = Math.min(defaultWidthPixels.toFloat() / pageWidth, defaultHeightPixels.toFloat() / pageHeight)
                val bitmap = Bitmap.createBitmap(
                    (pageWidth * scale).toInt(),
                    (pageHeight * scale).toInt(),
                    Bitmap.Config.ARGB_8888
                )

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)
                page.close()
            }
            pdfRenderer.close()
        } ?: throw IOException("Failed to open PdfRenderer.")
        return bitmaps
    }
}
