package com.example.notify.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.id)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* this is for later to edit profile */ }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Profile")
            }
        }
    ) { paddingValues ->
        BodyContent(Modifier.padding(paddingValues))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dummy User", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        ProfileDetail("Email", "example@uwaterloo.ca")
        ProfileDetail("Uploaded Documents", "10")
        ProfileDetail("Bookmarked Documents", "5")
        ProfileDetail("Likes amount", "50")
    }
}

@Composable
fun ProfileDetail(label: String, detail: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("$label:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(8.dp))
        Text(detail, style = MaterialTheme.typography.bodyMedium)
    }
}

