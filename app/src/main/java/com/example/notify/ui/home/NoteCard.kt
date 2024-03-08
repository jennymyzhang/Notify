package com.example.notify.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notify.R

@Composable
fun NoteList(navController: NavHostController,) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            for (i in 1..10) {
                NoteCard(
                    id = i.toString(),
                    navController = navController,
                    contentDescription = "This is a note",
                    title = i.toString()
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Spacer(modifier=Modifier.width(10.dp))
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            for (i in 1..10) {
                NoteCard(
                    id = i.toString(),
                    navController = navController,
                    contentDescription = "This is a note",
                    title = i.toString()
                )
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}
@Composable
fun NoteCard(
    id: String,
    navController: NavHostController,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val filename = "cheat_sheet.pdf"
    val fileInputStream = context.assets.open(filename)
    val byteArray = fileInputStream.readBytes()
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    Card(
        onClick = {navController.navigate(route = "note/{id}".replace(
            oldValue = "{id}",
            newValue = id
        ))},
        modifier = Modifier.fillMaxWidth(),
        elevation= CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter=painterResource(id = R.drawable.facebook),
//                bitmap = bitmap.asImageBitmap(),
                contentDescription=contentDescription,
                contentScale= ContentScale.Crop
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                ))
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
                contentAlignment = Alignment.BottomStart)
            {
                Text(title, style= TextStyle(color= Color.White, fontSize=16.sp))
            }
        }
    }
}
