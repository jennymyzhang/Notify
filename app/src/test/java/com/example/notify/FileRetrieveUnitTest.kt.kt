package com.example.notify

import com.example.notify.Services.fileRetrieve.fileInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any


//Here we DO NOT need this since this is an external layer!!!!
//Again it's calling firebase
class FileRetrieveUnitTest {
    @Test
    fun fetchLikesForPushKey_ReturnsLikesSuccessfully() {
        // Arrange
        val pushKey = "samplePushKey"
        val expectedLikes = 5
        val mockSnapshot = mock(DataSnapshot::class.java)
        val mockDatabaseReference = mock(DatabaseReference::class.java)
        `when`(mockDatabaseReference.child(pushKey)).thenReturn(mockDatabaseReference)
        `when`(mockSnapshot.child("likes").getValue(Int::class.java)).thenReturn(expectedLikes)
        Mockito.doAnswer { invocation ->
            val listener = invocation.arguments[0] as ValueEventListener
            listener.onDataChange(mockSnapshot)
            null
        }.`when`(mockDatabaseReference).addListenerForSingleValueEvent(any())

        val fileInfo = fileInfo() // Assuming a way to inject mockDatabaseReference
        var actualLikes: Int? = null

        // Act
        fileInfo.fetchLikesForPushKey(pushKey) { likes ->
            actualLikes = likes
        }

        // Assert
        assertEquals(expectedLikes, actualLikes)
    }

}