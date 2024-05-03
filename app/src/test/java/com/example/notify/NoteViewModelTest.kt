package com.example.notify.ui.note

import androidx.lifecycle.Observer
import com.example.notify.Services.fileRetrieve.fileInfo
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NoteScreenModelTest {

    private var viewModel: NoteScreenModel = NoteScreenModel()
    private val fileInfo: fileInfo = mockk()
    private val fileInfoMock: fileInfo = mockk(relaxed = true)

    @Test
    fun fetchLikes() = runBlocking {
        // Arrange
        val pushKey = "testPushKey"
        val expectedLikes = 42
        val observerSlot = slot<Observer<Int>>()
        val liveDataObserver = mockk<Observer<Int>>(relaxed = true)

        viewModel.like.observeForever(liveDataObserver)

        coEvery { fileInfoMock.fetchLikesForPushKey(eq(pushKey), any()) } answers {
            val callback = arg<(Int?) -> Unit>(1)
            callback.invoke(expectedLikes)
        }

        // Act
        viewModel.fetchLikes(pushKey)

        // Assert
        assertTrue(observerSlot.captured.equals(expectedLikes))

        viewModel.like.removeObserver(liveDataObserver)
    }

    @Test
    fun fetchCollects() = runBlocking {
        // Arrange
        val pushKey = "testPushKeyCollect"
        val expectedCollects = 15
        val liveDataObserver = mockk<Observer<Int>>(relaxed = true)

        coEvery { fileInfoMock.fetchColletsForPushKey(eq(pushKey), any()) } coAnswers {
            val callback = arg<(Int?) -> Unit>(1)
            callback(expectedCollects)
        }

        viewModel.collect.observeForever(liveDataObserver)

        // Act
        viewModel.fetchCollects(pushKey)

        // Assert
        assertEquals(expectedCollects, viewModel.collect.value)

        viewModel.collect.removeObserver(liveDataObserver)
    }
}
