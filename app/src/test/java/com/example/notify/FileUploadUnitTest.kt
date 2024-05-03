package com.example.notify

import com.example.notify.Services.UploadService.PdfFile
import junit.framework.TestCase.assertEquals
import org.junit.Test


class PdfFileTest {

    class PdfFileTest {

        @Test
        fun testPdf() {
            // Arrange
            val expectedFileName = "test.pdf"
            val expectedDownloadUrl = "http://example.com/test.pdf"
            val expectedUid = "12345"
            val expectedSubject = "Math"
            val expectedCourseNum = "101"
            val expectedTerm = "Fall"
            val expectedYear = "2023"
            val expectedLikes = 10
            val expectedCollects = 20
            val expectedUuid = "uuid12345"
            val expectedPushKey = "pushKey123"
            val expectedExtractedText = "This is a test."
            val expectedFirstPageImageBase64 = "base64ImageString"

            // Act
            val pdfFile = PdfFile(
                expectedFileName,
                expectedDownloadUrl,
                expectedUid,
                expectedSubject,
                expectedCourseNum,
                expectedTerm,
                expectedYear,
                expectedLikes,
                expectedCollects,
                expectedUuid,
                expectedPushKey,
                expectedExtractedText,
                expectedFirstPageImageBase64
            )

            // Assert
            assertEquals(expectedFileName, pdfFile.fileName)
            assertEquals(expectedDownloadUrl, pdfFile.downloadUrl)
            assertEquals(expectedUid, pdfFile.uid)
            assertEquals(expectedSubject, pdfFile.subject)
            assertEquals(expectedCourseNum, pdfFile.courseNum)
            assertEquals(expectedTerm, pdfFile.term)
            assertEquals(expectedYear, pdfFile.year)
            assertEquals(expectedLikes, pdfFile.likes)
            assertEquals(expectedCollects, pdfFile.collects)
            assertEquals(expectedUuid, pdfFile.uuid)
            assertEquals(expectedPushKey, pdfFile.pushKey)
            assertEquals(expectedExtractedText, pdfFile.extractedText)
            assertEquals(expectedFirstPageImageBase64, pdfFile.firstPageImageBase64)
        }

        @Test
        fun createUsingDefaultValue() {
            // Act
            val pdfFile = PdfFile()

            // Assert
            assertEquals("", pdfFile.fileName)
            assertEquals("", pdfFile.downloadUrl)
            assertEquals("", pdfFile.uid)
            assertEquals("", pdfFile.subject)
            assertEquals("", pdfFile.courseNum)
            assertEquals("", pdfFile.term)
            assertEquals("", pdfFile.year)
            assertEquals(0, pdfFile.likes)
            assertEquals(0, pdfFile.collects)
            assertEquals("", pdfFile.uuid)
            assertEquals("", pdfFile.pushKey)
            assertEquals("", pdfFile.extractedText)
            assertEquals("", pdfFile.firstPageImageBase64)
        }
    }
}