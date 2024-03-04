package com.lucagiorgetti.surprix.utility

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfGState
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.lucagiorgetti.surprix.BuildConfig
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.SurprixApplication.Companion.surprixContext
import com.lucagiorgetti.surprix.model.Surprise
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections
import java.util.Locale

object PDFUtils {
    @Throws(IOException::class)
    fun createMissingListPdfFile(activity: FragmentActivity, missingSurprises: List<Surprise>, path: String?) {
        try {
            val document = Document()
            //File newFile = File.createTempFile("prova.pdf", null, SurprixApplication.getSurprixContext().getCacheDir());
            val cacheFile = File(surprixContext.cacheDir, "doc/prova.pdf")
            cacheFile.createNewFile()
            val writer = PdfWriter.getInstance(document, FileOutputStream(cacheFile))
            val myEvent = MyEvent("logo_shape.jpg")
            writer.pageEvent = myEvent
            document.open()
            document.setPageSize(PageSize.A4)
            document.addCreationDate()
            document.addAuthor(getInstance().resources.getString(R.string.app_name))
            document.addCreator(getInstance().resources.getString(R.string.app_name))
            val hashMap = HashMap<String, List<Surprise>>()
            val colorAccent = BaseColor(ContextCompat.getColor(surprixContext, R.color.surprixBlue))
            val fontSize = 12.0f
            val fontName = BaseFont.createFont()
            val titleFont = Font(fontName, 20.0f, Font.BOLD, colorAccent)
            val title = String.format(Locale.getDefault(), "%s %s (%d)", getInstance().resources.getString(R.string.missings), getInstance().currentUser?.username, missingSurprises.size)
            addNewItem(document, title, Element.ALIGN_CENTER, titleFont)
            addLineSeparator(document, colorAccent)
            Collections.sort(missingSurprises) { obj: Surprise, surprise: Surprise -> obj.compareTo(surprise) }
            for (missing in missingSurprises) {
                val orderNumberFont = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
                try {
                    val surpriseString = String.format(Locale.getDefault(), "%s: %s - %s, %s, %s", missing.code, missing.description, missing.set_name, missing.set_producer_name, missing.set_year_name)
                    addNewItem(document, surpriseString, Element.ALIGN_LEFT, orderNumberFont)
                } catch (e: DocumentException) {
                    e.printStackTrace()
                }
            }
            document.close()
            val uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", cacheFile)
            val mime = getInstance().contentResolver.getType(uri)

            // Open file with user selected app
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, mime)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivity(intent)
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(DocumentException::class)
    private fun addLineSeparator(document: Document, colorAccent: BaseColor) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = colorAccent
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, font: Font) {
        val chunk = Chunk(text, font)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = align
        document.add(paragraph)
    }

    class MyEvent internal constructor(path: String?) : PdfPageEventHelper() {
        var image: Image

        init {
            val `is` = getInstance().assets.open(path!!)
            val bufferedInputStream = BufferedInputStream(`is`)
            val bmp = BitmapFactory.decodeStream(bufferedInputStream)
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 50, stream)
            val byteArray = stream.toByteArray()
            val jpg = Image.getInstance(byteArray)
            jpg.scaleToFit(500f, 700f)
            jpg.setAbsolutePosition(60f, 90f)
            image = jpg
        }

        override fun onEndPage(writer: PdfWriter, document: Document) {
            super.onEndPage(writer, document)
            try {
                val canvas = writer.directContent
                canvas.saveState()
                val state = PdfGState()
                state.setFillOpacity(0.2f)
                canvas.setGState(state)
                canvas.addImage(image)
                canvas.restoreState()
            } catch (e: DocumentException) {
                e.printStackTrace()
            }
        }
    }
}
