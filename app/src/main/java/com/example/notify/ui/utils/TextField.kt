package com.example.notify.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.notify.ui.theme.Black
import com.example.notify.ui.theme.focusedTextFieldText
import com.example.notify.ui.theme.textFieldContainer
import com.example.notify.ui.theme.unfocusedTextFieldText

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    trailing: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation =  VisualTransformation.None
) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(size = 10.dp),
        visualTransformation = visualTransformation,
        label = {
            Text(text = label,
                style = MaterialTheme.typography.labelMedium,
                color = uiColor)
        },
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
        ),
        trailingIcon = {
            TextButton(onClick = {}) {
                Text(text = trailing,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = uiColor)

            }
        }
    )
}
