

import com.example.notify.Services.AuthService.Authentication
import com.example.notify.Services.Utils.Resource
import com.example.notify.ui.signup.SignUpState
import com.example.notify.ui.signup.SignUpViewModel
import com.google.firebase.auth.AuthResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignUpViewModelTest {


    // Mock dependencies
    private val auth: Authentication = mockk()

    // Subject under test
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setup() {
        viewModel = SignUpViewModel(auth)
    }

    @Test
    fun signUpSuccess() = runBlocking {
        // Arrange
        val email = "j2466zha@uwaterloo.ca"
        val password = "123456"
        val firstName = "John"
        val lastName = "Doe"
        val expectedState = SignUpState(isSuccess = "Sign In Success ")
        val mockAuthResult: AuthResult = mockk()
        coEvery { auth.registerUser(email, password, firstName, lastName) } returns kotlinx.coroutines.flow.flow {
            emit(Resource.Loading())
            emit(Resource.Success(mockAuthResult))
        }

        val states = mutableListOf<SignUpState>()

        // Act
        val job = launch {
            viewModel.signUpState.toList(states)
        }
        viewModel.registerUser(email, password, firstName, lastName)

        // Assert
        TestCase.assertEquals(expectedState.isSuccess, states.last().isSuccess)

        job.cancel()
    }

    @Test
    fun signUpUserLoading() = runBlocking {
        // Arrange
        val email = "loading@example.com"
        val password = "loading123"
        val firstName = "John"
        val lastName = "Doe"
        val expectedState = SignUpState(isLoading = true)

        coEvery { auth.registerUser(email, password, firstName, lastName) } returns kotlinx.coroutines.flow.flow {
            emit(Resource.Loading())
        }

        val states = mutableListOf<SignUpState>()

        // Act
        val job = launch {
            viewModel.signUpState.toList(states)
        }
        viewModel.registerUser(email, password, firstName, lastName)

        // Assert
        TestCase.assertEquals(expectedState.isLoading, states.last().isLoading)

        job.cancel()
    }

    @Test
    fun signUpFailure() = runBlocking {
        // Arrange
        val email = "fail@example.com"
        val password = "fail123"
        val firstName = "John"
        val lastName = "Doe"
        val errorMessage = "Authentication failed"
        val expectedState = SignUpState(isError = errorMessage)

        coEvery { auth.registerUser(email, password, firstName, lastName) } returns kotlinx.coroutines.flow.flow {
            emit(Resource.Error(errorMessage))
        }

        val states = mutableListOf<SignUpState>()

        // Act
        val job = launch {
            viewModel.signUpState.toList(states)
        }
        viewModel.registerUser(email, password, firstName, lastName)

        // Assert
        TestCase.assertEquals(expectedState.isError, states.last().isError)

        job.cancel()
    }
}
