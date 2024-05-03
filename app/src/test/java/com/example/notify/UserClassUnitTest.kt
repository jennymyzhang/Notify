package com.example.notify
import com.example.notify.Services.UserService.UserImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert.assertEquals
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

// This UserImpl class is calling external Firebase service
// It's external dependencies so we have to use Mock
class UnitTest {
    private val firebaseAuth = FirebaseAuth.getInstance()
    @Test
    fun userid_isCorrect() { //using mock here to because its using FirebaseAuth and we cant test it with one userid because we all have different user ids depending on who the current user is
        // Arrange
        val mockedFirebaseAuth: FirebaseAuth = mock()
        val mockedFirebaseUser: FirebaseUser = mock()

        whenever(mockedFirebaseAuth.currentUser).thenReturn(mockedFirebaseUser)
        whenever(mockedFirebaseUser.uid).thenReturn("x2RKOeK6JBXx7gTo4s9mJnqtovk1")

        val user: UserImpl = UserImpl(mockedFirebaseAuth)

        // Act
        val uid = user.getCurrentUserId()

        // Assert
        assertEquals("x2RKOeK6JBXx7gTo4s9mJnqtovk1", uid)
    }

    @Test
    fun userEmail_isCorrect() {
        // Arrange
        val mockedFirebaseAuth: FirebaseAuth = mock()
        val mockedFirebaseUser: FirebaseUser = mock()

        whenever(mockedFirebaseAuth.currentUser).thenReturn(mockedFirebaseUser)
        whenever(mockedFirebaseUser.uid).thenReturn("x2RKOeK6JBXx7gTo4s9mJnqtovk1")

        val user: UserImpl = UserImpl(mockedFirebaseAuth)

        // Act
        val uid = user.getCurrentUserEmail()

        // Assert
        assertEquals("j2466zha@uwaterloo.ca", uid)
    }
}