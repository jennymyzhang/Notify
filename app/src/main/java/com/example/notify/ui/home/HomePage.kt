package com.example.notify.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.notify.R
import com.example.notify.Services.UploadService.PdfFile
import com.example.notify.ui.Route
import com.example.notify.ui.homePage.HomePageViewModel
import com.example.notify.ui.search.NoteList
import com.example.notify.ui.theme.Black
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun HomePage(navController: NavHostController) {
    val homePageViewModel: HomePageViewModel = viewModel()
    LaunchedEffect(Unit) {
        homePageViewModel.retrievePdfFiles()
    }
    // Tom just use this pdfFiles as the entire object, now it gets loaded automatically, so we are good to use this variable
    val pdfFiles by homePageViewModel.pdfFiles.observeAsState(initial = emptyList())
    pdfFiles.forEach { pdfFile ->
        Log.d("HomePageViewModel", "Retrieved PDF File: ${pdfFile.fileName}")
    }
    val auth: FirebaseAuth = Firebase.auth
    val currentUserId = auth.currentUser?.uid

    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.45f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (currentUserId != null) {
                    TopSection(navController = navController, currentUserId = currentUserId)
                }
            },
            bottomBar = {
                if (currentUserId != null) {
                    Bottom (modifier = Modifier.fillMaxWidth(), currentUserId=currentUserId, navController=navController)
                }
            }
        ) { paddingValues ->
            Center(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                navController = navController,
                pdfFiles = pdfFiles,
                currentUserId = currentUserId
            )
        }
    }
}

@Composable
fun Bottom(modifier: Modifier = Modifier, currentUserId: String, navController: NavHostController) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    Box(modifier) {
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Card(
                onClick = {navController.navigate(route = "home")},
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                ),
            ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            tint = uiColor,
                            imageVector = rememberHome(uiColor),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Text("Home", textAlign = TextAlign.Center, color=uiColor)
                    }


            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF81D4FA),
                ),
                onClick = {navController.navigate(route = "upload")},
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(10.dp)
                    .background(color=Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = rememberAdd(Color.White),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Card(
                onClick = {navController.navigate(route = "profile/$currentUserId/$currentUserId/posts")},
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        tint = uiColor,
                        imageVector = rememberPerson(uiColor),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Profile", textAlign = TextAlign.Center, color=uiColor)
                }
            }
        }
    }
}

@Composable
fun Center(modifier: Modifier=Modifier, navController: NavHostController, pdfFiles: List<PdfFile>,
           currentUserId: String?) {
    val sortedPdfFiles: List<PdfFile> = pdfFiles.sortedByDescending { it.likes * 0.7 + it.collects * 0.3 }
    Column(modifier) {
        NoteList(sortedPdfFiles, navController, currentUserId)
    }
}

@Composable
private fun TopSection(navController: NavHostController, currentUserId: String) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.padding(bottom=(5.dp))
    ) {
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
                        .clickable {
                            // Assuming you have the currentUserId available here
                            val userId = currentUserId ?: "defaultUserId" // Use a default or handle the null case as needed
                            navController.navigate(Route.SearchScreen().createRoute(userId))
                        }
                        .size(40.dp)
                        .padding(end = 8.dp), // Add some space between the search icon and the user icon
                    tint = uiColor
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_face_2_24),
                    contentDescription = "Big Elephant",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.Black, CircleShape)
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
                        navController.navigate("profile/$currentUserId/$currentUserId/posts")
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

@Composable
fun rememberHome(color:Color = Color.Black): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "home",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.792f, 35.417f)
                quadToRelative(-1.459f, 0f, -2.5f, -1.042f)
                quadToRelative(-1.042f, -1.042f, -1.042f, -2.5f)
                verticalLineTo(16.542f)
                quadToRelative(0f, -0.875f, 0.375f, -1.604f)
                quadToRelative(0.375f, -0.73f, 1.042f, -1.23f)
                lineToRelative(10.208f, -7.666f)
                quadToRelative(0.5f, -0.334f, 1.042f, -0.521f)
                quadToRelative(0.541f, -0.188f, 1.083f, -0.188f)
                quadToRelative(0.583f, 0f, 1.104f, 0.188f)
                quadToRelative(0.521f, 0.187f, 1.021f, 0.521f)
                lineToRelative(10.208f, 7.666f)
                quadToRelative(0.667f, 0.5f, 1.042f, 1.23f)
                quadToRelative(0.375f, 0.729f, 0.375f, 1.604f)
                verticalLineToRelative(15.333f)
                quadToRelative(0f, 1.458f, -1.042f, 2.5f)
                quadToRelative(-1.041f, 1.042f, -2.5f, 1.042f)
                horizontalLineToRelative(-6.625f)
                verticalLineTo(23.042f)
                horizontalLineToRelative(-7.125f)
                verticalLineToRelative(12.375f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberPerson(color:Color = Color.Black): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "person",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 19.667f)
                quadToRelative(-3f, 0f, -4.958f, -1.959f)
                quadToRelative(-1.959f, -1.958f, -1.959f, -4.958f)
                reflectiveQuadToRelative(1.959f, -4.958f)
                quadTo(17f, 5.833f, 20f, 5.833f)
                reflectiveQuadToRelative(4.958f, 1.959f)
                quadToRelative(1.959f, 1.958f, 1.959f, 4.958f)
                reflectiveQuadToRelative(-1.959f, 4.958f)
                quadTo(23f, 19.667f, 20f, 19.667f)
                close()
                moveTo(9.792f, 33.958f)
                quadToRelative(-1.5f, 0f, -2.521f, -1.02f)
                quadToRelative(-1.021f, -1.021f, -1.021f, -2.521f)
                verticalLineTo(29.25f)
                quadToRelative(0f, -1.667f, 0.854f, -2.938f)
                quadToRelative(0.854f, -1.27f, 2.229f, -1.937f)
                quadToRelative(2.75f, -1.25f, 5.375f, -1.896f)
                quadToRelative(2.625f, -0.646f, 5.292f, -0.646f)
                quadToRelative(2.708f, 0f, 5.312f, 0.646f)
                quadToRelative(2.605f, 0.646f, 5.313f, 1.896f)
                quadToRelative(1.417f, 0.625f, 2.271f, 1.896f)
                quadToRelative(0.854f, 1.271f, 0.854f, 2.979f)
                verticalLineToRelative(1.208f)
                quadToRelative(0f, 1.459f, -1.021f, 2.48f)
                quadToRelative(-1.021f, 1.02f, -2.521f, 1.02f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberAdd(color: Color = Color.Black): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "add",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 31.875f)
                quadToRelative(-0.75f, 0f, -1.25f, -0.5f)
                reflectiveQuadToRelative(-0.5f, -1.25f)
                verticalLineTo(21.75f)
                horizontalLineTo(9.875f)
                quadToRelative(-0.75f, 0f, -1.271f, -0.521f)
                quadToRelative(-0.521f, -0.521f, -0.521f, -1.229f)
                quadToRelative(0f, -0.75f, 0.521f, -1.271f)
                quadToRelative(0.521f, -0.521f, 1.271f, -0.521f)
                horizontalLineToRelative(8.375f)
                verticalLineTo(9.833f)
                quadToRelative(0f, -0.708f, 0.5f, -1.229f)
                quadToRelative(0.5f, -0.521f, 1.25f, -0.521f)
                reflectiveQuadToRelative(1.271f, 0.521f)
                quadToRelative(0.521f, 0.521f, 0.521f, 1.229f)
                verticalLineToRelative(8.375f)
                horizontalLineToRelative(8.333f)
                quadToRelative(0.75f, 0f, 1.271f, 0.521f)
                quadToRelative(0.521f, 0.521f, 0.521f, 1.271f)
                quadToRelative(0f, 0.708f, -0.521f, 1.229f)
                quadToRelative(-0.521f, 0.521f, -1.271f, 0.521f)
                horizontalLineToRelative(-8.333f)
                verticalLineToRelative(8.375f)
                quadToRelative(0f, 0.75f, -0.521f, 1.25f)
                reflectiveQuadToRelative(-1.271f, 0.5f)
                close()
            }
        }.build()
    }
}