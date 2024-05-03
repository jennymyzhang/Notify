package com.example.notify
import com.example.notify.Services.Utils.Resource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ResourceUnitTest {

    @Test
    fun testSuccess() {
        // Arrange
        val expectedData = "Test Success"

        // Act
        val result = Resource.Success(expectedData)

        // Assert
        assertEquals(expectedData, result.data)
        assertNull(result.message)
    }

    @Test
    fun testError() {
        // Arrange
        val errorMessage = "An error occurred"
        val errorData: String? = "Input data does not meet format"

        // Act
        val result = Resource.Error<String>(errorMessage, errorData)

        // Assert
        assertEquals(errorMessage, result.message)
        assertEquals(errorData, result.data)
    }

    @Test
    fun testLoading() {
        // Arrange
        val loadingData: String? = "Process is loading...please wait"

        // Act
        val result = Resource.Loading(loadingData)

        // Assert
        assertEquals(loadingData, result.data)
        assertNull(result.message)
    }
}