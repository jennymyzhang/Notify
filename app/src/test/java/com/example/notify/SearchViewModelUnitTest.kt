package com.example.notify

import com.example.notify.Services.UploadService.FileUploadImpl
import com.example.notify.Services.UploadService.PdfFile
import com.example.notify.ui.search.SearchModel
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchModelTest {

    private lateinit var searchModel: SearchModel
    private val fileUploadService = mockk<FileUploadImpl>()

    @Before
    fun setUp() {
        // Mock the FileUploadImpl service or any other dependencies
        searchModel = SearchModel(fileUploadService)
    }

    @Test
    fun filteredPdfFile() = runBlocking {
        // Arrange
        val samplePdfFiles = listOf(
            PdfFile("Math235.pdf", "url", "uid", "Math", "235", "W", "2024", 0, 0, "uid", "pushkey", "basic math introduction", ""),
        )
        searchModel = SearchModel(fileUploadService).apply {
            _pdfFiles.value = samplePdfFiles
        }

        // Act
        searchModel.onSearchTextChange("math")
        val result = searchModel.filteredPdfFiles.first()

        // Assert
        assertTrue(result.size == 1)
        assertTrue(result.any { it.fileName == "math101.pdf" })
    }

    @Test
    fun calculateScore() {
        val pdfFile = PdfFile(
            "Math235.pdf", "url", "uid", "Math", "235", "W", "2024", 0, 0, "uid", "pushkey", "basic math introduction", ""
        )
        val searchModel = SearchModel(fileUploadService)

        // Act
        val score = searchModel.calculateScore(pdfFile, "math introduction")

        // Assert
        assertEquals(10, score)
    }

    @Test
    fun `calculateScore handles null values`() {
        // Arrange
        val pdfFile = PdfFile("", "", "", "", "", "", "", 0,0, "", "", "", "")
        val searchModel = SearchModel(fileUploadService)

        // Act
        val score = searchModel.calculateScore(pdfFile, "math")

        // Assert
        assertEquals(0, score)
    }

    @Test
    fun calculateScoreEmptyQuery() {
        // Arrange
        val pdfFile = PdfFile("Math235.pdf", "url", "uid", "Math", "235", "W", "2024", 0, 0, "uid", "pushkey", "doesn't include anything", "")
        val searchModel = SearchModel(fileUploadService)

        // Act
        val score = searchModel.calculateScore(pdfFile, "")

        // Assert
        assertEquals(0, score)
    }
}