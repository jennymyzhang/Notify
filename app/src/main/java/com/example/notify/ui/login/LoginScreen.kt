package com.example.notify.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notify.R
import com.example.notify.ui.theme.Black
import com.example.notify.ui.theme.Roboto
import com.example.notify.ui.theme.buttonContainer
import com.example.notify.ui.theme.buttonContent
import com.example.notify.ui.theme.unfocusedTextFieldText
import com.example.notify.ui.utils.LoginTextField
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(onSignUpClick: (Int) -> Unit,
                onLoginClick: () -> Unit) {
    Surface() {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection()
            Spacer(modifier = Modifier.height(36.dp))
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)) {
                LogInSection(onLoginClick)
                Spacer(modifier = Modifier.height(36.dp))
                CreateNew(onSignUpClick)
            }
        }
    }
}

@Composable
private fun CreateNew(onSignUpClick: (Int) -> Unit) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black

    Box(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.8f)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ClickableText(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append(stringResource(id = R.string.noAccount))
            }
            withStyle(
                style = SpanStyle(
                    color = uiColor,
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                )
            )
            {
                append(" ")
                append(stringResource(id = R.string.createNew))
            }
        },
            onClick = onSignUpClick)
    }
}


@Composable
private fun LogInSection(onLoginClick: () -> Unit,
                         viewModel: LogInViewModel = hiltViewModel()) {

    val (email, onEmailChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, onPasswordChange) = rememberSaveable {
        mutableStateOf("")
    }

    val showAlertMessage = rememberSaveable {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.logInState.collectAsState(initial = null)

    if (showAlertMessage.value) {
        AlertDialog(
            onDismissRequest = {showAlertMessage.value = false},
            title = {Text("User Not Found")},
            text = { Text(text = "User not found with the given email, please sign up first!")},
            confirmButton = {
                Button(
                    onClick = {showAlertMessage.value = false},
                    modifier = Modifier.padding(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.buttonContainer,
                        contentColor = MaterialTheme.colorScheme.buttonContent
                    )
                ){
                    Text(text = "confirm")
                }
            })
    }

    LoginTextField(
        value = email,
        label = stringResource(id = R.string.email),
        trailing = "",
        modifier = Modifier.fillMaxWidth(),
        onValueChange = onEmailChange
    )

    Spacer(modifier = Modifier.height(15.dp))

    LoginTextField(
        value = password,
        label = stringResource(id = R.string.password),
        modifier = Modifier.fillMaxWidth(),
        trailing = "",
        visualTransformation = PasswordVisualTransformation(),
        onValueChange = onPasswordChange
    )

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = {
            scope.launch {
                if(email == "" || !email.endsWith("@uwaterloo.ca")) {
                    Toast.makeText(context, "Please enter a valid Waterloo Email", Toast.LENGTH_LONG).show()
                } else if(password == "") {
                    Toast.makeText(context, "Please enter your password", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.loginUser(email, password)
                }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.buttonContainer,
            contentColor = MaterialTheme.colorScheme.buttonContent
        ),
        shape = RoundedCornerShape(size = 4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.login),
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
                onLoginClick()
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

@Composable
private fun TopSection() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.46f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier.padding(top = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.app_logo),
                tint = uiColor
            )

            Column {
                Text(
                    text = stringResource(id = R.string.notify),
                    style = MaterialTheme.typography.headlineMedium,
                    color = uiColor
                )

                Text(
                    text = stringResource(id = R.string.find_note),
                    style = MaterialTheme.typography.titleMedium,
                    color = uiColor
                )
            }
        }

        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            color = uiColor
        )
    }
}
