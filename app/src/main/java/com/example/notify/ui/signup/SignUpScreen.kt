package com.example.notify.ui.signup

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.notify.R
import com.example.notify.ui.theme.Black
import com.example.notify.ui.theme.buttonContainer
import com.example.notify.ui.theme.buttonContent
import com.example.notify.ui.utils.LoginTextField
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
        navHostController: NavHostController,
        viewModel: SignUpViewModel = hiltViewModel()
) {
    val (firstName, onFirstNameChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (lastName, onLastNameChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (email, onEmailChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, onPasswordChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (confirmPassword, onConfirmPasswordChange) = rememberSaveable {
        mutableStateOf("")
    }

    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signUpState.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "back",
                modifier = Modifier
                    .clickable { navHostController.navigate("login") }
                    .size(30.dp),// Add some space between the search icon and the user icon
                tint = uiColor
            )
        }
        Text(
            text = stringResource(id = R.string.signUp),
            modifier = Modifier
                .padding(vertical = 20.dp)
                .align(Alignment.Start),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor)

        LoginTextField(
            label = stringResource(id = R.string.firstname),
            value = firstName,
            trailing = "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onFirstNameChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginTextField(
            label = stringResource(id = R.string.lastName),
            value = lastName,
            trailing = "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onLastNameChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginTextField(
            label = stringResource(id = R.string.email),
            value = email,
            trailing = "Waterloo Email",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onEmailChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginTextField(
            label = stringResource(id = R.string.password),
            value = password,
            trailing = "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginTextField(
            label = stringResource(id = R.string.confirmPassword),
            value = confirmPassword,
            trailing = "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onConfirmPasswordChange,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                if (!email.endsWith("@uwaterloo.ca")) {
                    Toast.makeText(context,
                        "Please enter a valid waterloo email",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (firstName =="") {
                    Toast.makeText(context,
                        "Please enter your first name",
                        Toast.LENGTH_LONG
                    ).show()

                } else if (lastName == "") {
                    Toast.makeText(context,
                        "Please enter your last name",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (password == "" || confirmPassword == "") {
                    Toast.makeText(context,
                        "Please enter your password and confirm it",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(
                        context,
                        "Please enter your password and confirm it",
                        Toast.LENGTH_LONG
                    ).show()
                }else {
                    scope.launch {
                        viewModel.registerUser(email, password, firstName, lastName)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.buttonContainer,
                contentColor = MaterialTheme.colorScheme.buttonContent
            ),
            shape = RoundedCornerShape(size = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.signUp),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (state.value?.isLoading == true) {
                CircularProgressIndicator()
            }
        }

        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                    navHostController.navigate("Login")
                }
            }
        }
        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotBlank() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

