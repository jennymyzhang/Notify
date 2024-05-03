package com.example.notify.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notify.ui.home.HomePage
import com.example.notify.ui.login.LoginScreen
import com.example.notify.ui.note.NoteScreen
import com.example.notify.ui.profile.ProfileScreen
import com.example.notify.ui.search.SearchScreen
import com.example.notify.ui.signup.SignUpScreen
import com.example.notify.ui.upload.UploadScreen


sealed class Route {
    data class LoginScreen(val name:String = "Login"): Route()
    data class SignUpScreen(val name:String = "Signup"): Route()
    data class SearchScreen(val name: String = "Search/{userId}") : Route() {
        fun createRoute(userId: String) = "Search/$userId"
    }
    data class HomeScreen(val name:String = "Home"): Route()
    data class ProfileScreen(val name:String = "Profile"): Route()
    data class NoteScreen(val name:String = "Note"): Route()
    data class UploadScreen(val name:String = "Upload"): Route()

}
@Composable
fun Navigation(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Route.LoginScreen().name) {
        composable(route = Route.HomeScreen().name) {
            HomePage(navHostController)
        }
        composable(route = Route.LoginScreen().name) {
            LoginScreen(
                onSignUpClick = {
                    navHostController.navigate(
                        Route.SignUpScreen().name
                    )
                },
                onLoginClick = {
                    navHostController.navigate(
                        Route.HomeScreen().name
                    )
                }
            )
        }
        composable(route = Route.SignUpScreen().name) {
            SignUpScreen(navHostController)
        }
        composable(
            route = Route.SearchScreen().name,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Retrieve the user ID from the arguments
            val userId = backStackEntry.arguments?.getString("userId")

            // Call SearchScreen and pass the retrieved userId
            SearchScreen(
                onBackClick = { navHostController.popBackStack() },
                navController = navHostController,
                currentUserId = userId  // Passing the retrieved userId
            )
        }
        composable(route = Route.ProfileScreen().name+"/{id}/{currentUserId}/{currentDisplay}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType},
                navArgument("currentUserId") { type = NavType.StringType},
            )) { args ->
            val id = args.arguments?.getString("id")
            val currentUserId = args.arguments?.getString("currentUserId")
            val currentDisplay = args.arguments?.getString("currentDisplay")
            if (id != null && currentUserId != null && currentDisplay != null) {
                ProfileScreen(id, currentUserId, currentDisplay, navHostController)
            }
        }
        composable(
            route = Route.NoteScreen().name+"/{id}/{downloadUrl}/{pushKey}/{userId}/{fileName}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType},
                navArgument("downloadUrl") { type = NavType.StringType},
                navArgument("pushKey") { type = NavType.StringType},
                navArgument("userId") { type = NavType.StringType},
                navArgument("fileName") { type = NavType.StringType},
            )
        )
        {args ->
            val id = args.arguments?.getString("id")
            val downloadUrl = args.arguments?.getString("downloadUrl")
            val pushKey = args.arguments?.getString("pushKey")
            val currentUserId = args.arguments?.getString("userId")
            val fileName = args.arguments?.getString("fileName")
            if (downloadUrl != null && id != null && pushKey != null && currentUserId != null && fileName != null) {
                NoteScreen(id, downloadUrl, pushKey, currentUserId, fileName, navHostController)
            }
        }
        composable(route = Route.UploadScreen().name) {
            UploadScreen(navHostController)
        }
    }
}
