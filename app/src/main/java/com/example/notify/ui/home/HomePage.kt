package com.example.notify.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notify.R
import com.example.notify.ui.theme.Black

@Composable
fun HomePage(navController: NavHostController) {
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection(navController)
            Spacer(modifier = Modifier.height(36.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Preview Files located here", modifier = Modifier.align(Alignment.CenterHorizontally))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate("home") }) {
                        Text("Home")
                    }
                    Button(onClick = { navController.navigate("search") }) {
                        Text("Search")
                    }
                    Button(onClick = { navController.navigate("profile") }) {
                        Text("Profile")
                    }
                }
            }
        }
    }
}

@Composable
private fun TopSection(navController: NavHostController) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.45f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .clickable { navController.navigate("Search") }
                        .size(40.dp)
                        .padding(end = 8.dp), // Add some space between the search icon and the user icon
                    tint = uiColor
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color.White, shape = CircleShape)
                        .clickable { expanded = !expanded }
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = (-8).dp, y = 5.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("Profile") },
                    onClick = {
                        expanded = false
                        navController.navigate("profile")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Setting") },
                    onClick = {
                        expanded = false
                        navController.navigate("setting")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Log Out") },
                    onClick = {
                        expanded = false
                        navController.navigate("Login")
                    }
                )
            }
        }
    }
}