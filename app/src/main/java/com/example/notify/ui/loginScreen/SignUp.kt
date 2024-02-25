package com.example.notify.ui.loginScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.notify.R
import com.example.notify.ui.theme.Black
import com.example.notify.ui.theme.buttonContainer
import com.example.notify.ui.theme.buttonContent

@Composable
fun SignUpScreen(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            onClick = {},
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
    }
}

