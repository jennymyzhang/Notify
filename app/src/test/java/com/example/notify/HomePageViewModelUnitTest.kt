
import com.example.notify.Services.UploadService.FileUploadImpl
import com.example.notify.Services.UploadService.PdfFile
import com.example.notify.Services.UploadService.PdfFilesRetrievalCallback
import com.example.notify.ui.homePage.HomePageViewModel
import io.mockk.every
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Test

class HomePageViewModelTest {


    private var viewModel = HomePageViewModel()
    private lateinit var fileUploadService: FileUploadImpl



    @Test
    fun retrievePdfFilesSuccess() {
        // Arrange
        val pdfFiles = listOf(PdfFile("Math235.pdf", "url", "uid", "Math", "235", "W", "2024", 0, 0, "uid", "pushkey", "basic math introduction", ""))

        val callbackSlot = slot<PdfFilesRetrievalCallback>()

        every { fileUploadService.retrieveAllPdfFiles(capture(callbackSlot)) } answers {
            callbackSlot.captured.onSuccess(pdfFiles)
        }

        // Act
        viewModel.retrievePdfFiles()

        // Assert
        assertEquals("PDF files retrieved successfully!", viewModel._message.value)
        assertEquals(pdfFiles, viewModel.pdfFiles.value)
    }

    @Test
    fun retrievePdfFilesNoPdf() {
        // Arrange
        val callbackSlot = slot<PdfFilesRetrievalCallback>()

        every { fileUploadService.retrieveAllPdfFiles(capture(callbackSlot)) } answers {
            callbackSlot.captured.onSuccess(emptyList())
        }

        // Act
        viewModel.retrievePdfFiles()

        // Assert
        assertEquals("No PDF files found.", viewModel._message.value)
        assertEquals(emptyList<PdfFile>(), viewModel.pdfFiles.value)
    }

    @Test
    fun retrievePdfFilesError() {
        // Arrange
        val errorMessage = "Error retrieving PDF files"
        val callbackSlot = slot<PdfFilesRetrievalCallback>()

        every { fileUploadService.retrieveAllPdfFiles(capture(callbackSlot)) } answers {
            callbackSlot.captured.onError(errorMessage)
        }

        // Act
        viewModel.retrievePdfFiles()

        // Assert
        assertEquals("Error retrieving PDF files: $errorMessage", viewModel._message.value)
        assertEquals(null, viewModel.pdfFiles.value) // Ensure pdfFiles value remains null on error
    }
}