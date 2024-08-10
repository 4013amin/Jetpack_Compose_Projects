import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.ptoject.data.ModelsProjects.ModelProjects
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream

fun generatePdf(context: Context, data: ModelProjects, fileName: String): File {
    val downloadsDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloadsDirectory, fileName)
    FileOutputStream(file).use { outputStream ->
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        // Add data to the document
        document.add(Paragraph("ID: ${data.id}").setFontColor(ColorConstants.BLACK))
        document.add(Paragraph("Title: ${data.title}").setFontColor(ColorConstants.BLACK))
        document.add(Paragraph("Body: ${data.body}").setFontColor(ColorConstants.BLACK))
        document.add(Paragraph("User ID: ${data.userId}").setFontColor(ColorConstants.BLACK))

        document.close()
    }
    return file
}

fun openPdf(context: Context, file: File) {
    val intent = Intent(Intent.ACTION_VIEW)
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    intent.setDataAndType(uri, "application/pdf")
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    context.startActivity(intent)
}
