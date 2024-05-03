import com.example.notify.Services.AuthService.UserData
import org.junit.Assert.assertTrue
import org.junit.Test

class UserDataTest {

    @Test
    fun userDataNonEmpty() {
        // Arrange
        val firstName = "John"
        val lastName = "Doe"
        val uid = "123456"

        // Act
        val userData = UserData(firstName, lastName, uid)

        // Assert
        assertTrue(userData.firstName == firstName)
        assertTrue(userData.lastName == lastName)
        assertTrue(userData.uid == uid)
    }

    @Test
    fun UserDataEmpty() {
        // Arrange
        val firstName = ""
        val lastName = ""
        val uid = ""

        // Act
        val userData = UserData()

        // Assert
        assertTrue(userData.firstName == firstName)
        assertTrue(userData.lastName == lastName)
        assertTrue(userData.uid == uid)
    }
}