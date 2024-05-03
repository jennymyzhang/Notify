package com.example.notify


import com.example.notify.Services.AuthService.Authentication
import com.example.notify.Services.UserService.User
import com.example.notify.Services.Utils.Resource
import com.example.notify.ui.login.LogInState
import com.example.notify.ui.login.LogInViewModel
import com.google.firebase.auth.AuthResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LogInViewModelTest {


    // Mock dependencies
    private val auth: Authentication = mockk()
    private val user: User = mockk()

    // Subject under test
    private lateinit var viewModel: LogInViewModel

    @Before
    fun setup() {
        viewModel = LogInViewModel(auth, user)
    }

    @Test
    fun loginSuccess() = runBlocking {
        // Arrange
        val email = "j2466zha@uwaterloo.ca"
        val password = "123456"
        val expectedState = LogInState(isSuccess = "Sign In Success ")
        val mockAuthResult: AuthResult = mockk()
        coEvery { auth.loginUser(email, password) } returns kotlinx.coroutines.flow.flow {
            emit(Resource.Loading())
            emit(Resource.Success(mockAuthResult))
        }

        val states = mutableListOf<LogInState>()

        // Act
        val job = launch {
            viewModel.logInState.toList(states)
        }
        viewModel.loginUser(email, password)

        // Assert
        assertEquals(expectedState.isSuccess, states.last().isSuccess)

        job.cancel()
    }

    @Test
    fun loginUserLoading() = runBlocking {
        // Arrange
        val email = "loading@example.com"
        val password = "loading123"
        val expectedState = LogInState(isLoading = true)

        coEvery { auth.loginUser(email, password) } returns kotlinx.coroutines.flow.flow {
            emit(Resource.Loading())
        }

        val states = mutableListOf<LogInState>()

        // Act
        val job = launch {
            viewModel.logInState.toList(states)
        }
        viewModel.loginUser(email, password)

        // Assert
        assertEquals(expectedState.isLoading, states.last().isLoading)

        job.cancel()
    }

    @Test
    fun loginFailure() = runBlocking {
        // Arrange
        val email = "fail@example.com"
        val password = "fail123"
        val errorMessage = "Authentication failed"
        val expectedState = LogInState(isError = errorMessage)

        coEvery { auth.loginUser(email, password) } returns kotlinx.coroutines.flow.flow {
            emit(Resource.Error(errorMessage))
        }

        val states = mutableListOf<LogInState>()

        // Act
        val job = launch {
            viewModel.logInState.toList(states)
        }
        viewModel.loginUser(email, password)

        // Assert
        assertEquals(expectedState.isError, states.last().isError)

        job.cancel()
    }
}
