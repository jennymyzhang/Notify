
import com.example.notify.Services.AuthService.AuthenticationImpl
import com.example.notify.Services.Utils.Resource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/* AAA Style
1. Arrange:
• Setup the conditions for your test.
• Initialize variables, load data, setup any dependencies that you might need.
• Do NOT reuse anything from a different test.
2. Act:
• Execute the functionality that you want to test and capture the results.
3. Assert:
• Check that the actual and expected results match.
• Use asserts appropriately
 */
//assertTrue
//assertEquals
//assertNotEquals
//assertFalse


//Here we DO NOT need this since this is an external layer!!!!
//The authentication is calling firebase
class AuthenticationImplTest {

    private val mockFirebaseAuth: FirebaseAuth = mock()
    private val mockDatabaseReference: DatabaseReference = mock()
    private val authImpl = AuthenticationImpl(mockFirebaseAuth, mockDatabaseReference)

    @Test
    fun loginUser() = runBlocking {
        // Arrange
        val email = "j2466zha@uwaterloo.ca"
        val password = "123456"
        val mockAuthResult: AuthResult = mock()
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(Tasks.forResult(mockAuthResult))

        // Act
        val result = authImpl.loginUser(email, password).toList()

        // Assert
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
    }

    @Test
    fun registerUser() = runBlocking {
        // Arrange
        val email = "test@uwaterloo.ca"
        val password = "password123"
        val firstName = "John"
        val lastName = "Doe"
        val mockAuthResult: AuthResult = mock()
        val mockUser: FirebaseUser = mock()

        whenever(mockUser.uid).thenReturn("uid123")
        whenever(mockAuthResult.user).thenReturn(mockUser)

        // Simulating a successful task completion
        whenever(mockFirebaseAuth.createUserWithEmailAndPassword(email, password)).thenReturn(Tasks.forResult(mockAuthResult))

        // Act
        val result = authImpl.registerUser(email, password, firstName, lastName).toList()

        // Assert
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
    }

    @Test
    fun loginUserError() = runBlocking {
        // Arrange
        val email = "j2466zha@uwaterloo.ca"
        val password = "wrongpassword"
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(email, password)).thenThrow(RuntimeException("An error occurred"))
        val authImpl = AuthenticationImpl(mockFirebaseAuth, mockDatabaseReference)

        // Act
        val result = authImpl.loginUser(email, password).toList()

        // Assert
        assertTrue("First emission should be Loading", result[0] is Resource.Loading)
        assertTrue("Second emission should be Error", result[1] is Resource.Error)
    }

    @Test
    fun registerUserError() = runBlocking {
        // Arrange
        val email = "6za@uwaterloo.ca"
        val password = "password123"
        val firstName = "John"
        val lastName = "Doe"
        val exceptionMessage = "Registration failed"
        whenever(mockFirebaseAuth.createUserWithEmailAndPassword(email, password)).thenThrow(RuntimeException(exceptionMessage))
        val authImpl = AuthenticationImpl(mockFirebaseAuth, mockDatabaseReference)

        // Act
        val result = authImpl.registerUser(email, password, firstName, lastName).toList()

        // Assert
        assertTrue("First emission should be Loading", result[0] is Resource.Loading)
        assertTrue("Second emission should be Error", result[1] is Resource.Error)
        assertEquals("Error message should match", exceptionMessage, (result[1] as Resource.Error).message)
    }
}
