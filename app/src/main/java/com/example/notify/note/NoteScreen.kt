package com.example.notify.ui.note

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.notify.R
import com.example.notify.databinding.PdfViewBinding
import com.example.notify.ui.theme.Black

@Composable
fun NoteScreen(id: String?, navController: NavHostController) {
    val context = LocalContext.current
    val filename = "cheat_sheet.pdf"
    val fileInputStream = context.assets.open(filename)
    val byteArray = fileInputStream.readBytes()

    PdfView(byteArray = byteArray, navController=navController)
}

@Composable
fun PdfView(
    byteArray: ByteArray,
    navController: NavHostController
) {
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
            topBar = { TopSection(navController = navController) },
            bottomBar = { Bottom (modifier = Modifier.fillMaxWidth()) }
        ) { paddingValues ->
            Center(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .background(Color.Transparent),
                byteArray = byteArray
            )
        }
    }

}

@Composable
private fun Center(modifier: Modifier=Modifier, byteArray: ByteArray) {
    Box(modifier) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 10.dp, 0.dp, 10.dp),
            factory = { context ->
                PdfViewBinding.inflate(LayoutInflater.from(context))
                    .apply {
                        pdfView.fromBytes(byteArray).load()
                    }
                    .root
            },
        )
    }

}

@Composable
private fun Bottom(modifier:Modifier = Modifier) {
    var favorite by remember { mutableIntStateOf(0) }
    var dislike by remember { mutableIntStateOf(0) }
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    Box(modifier=modifier) {
        Row(modifier=Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically) {
            FavoriteButton(
                addFavorite={favorite += 1},
                subFavorite={favorite -= 1},
                color=uiColor
            )
            Text(favorite.toString(), color=uiColor, modifier=Modifier.padding(end=5.dp))
            DislikeButton(
                addDislike={dislike += 1},
                subDislike={dislike -= 1},
                color=uiColor
            )
            Text(dislike.toString(), color=uiColor, modifier=Modifier.padding(end=20.dp))
        }
    }
}

@Composable
private fun TopSection(navController: NavHostController) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    var expanded by remember { mutableStateOf(false) }
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
            Button(
                onClick= { navController.popBackStack() },
                modifier= Modifier
                    .height(40.dp)
                    .width(40.dp),
                contentPadding = PaddingValues(1.dp),
                shape= RectangleShape,
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint=uiColor
                )
            }
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

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    addFavorite: ()->Unit,
    subFavorite: ()->Unit,
) {
    var isFavorite by remember { mutableStateOf(false) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            if (isFavorite) {
                subFavorite()
            } else {
                addFavorite()
            }
            isFavorite = !isFavorite
        }
    ) {
        Icon(
            tint = color,
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}

@Composable
fun DislikeButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    addDislike: ()->Unit,
    subDislike: ()->Unit,
) {
    var isDislike by remember { mutableStateOf(false) }

    IconToggleButton(
        checked = isDislike,
        onCheckedChange = {
            if (isDislike) {
                subDislike()
            } else {
                addDislike()
            }
            isDislike = !isDislike
        },
    ) {
        Icon(
            tint = color,
            imageVector = if (isDislike) {
                dislikeFilled(color)
            } else {
                dislikeBorder(color)
            },
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }

}

@Composable
fun dislikeBorder(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "heart_broken",
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
                moveTo(16.083f, 32.042f)
                quadToRelative(-4.208f, -4.167f, -6.708f, -6.834f)
                quadToRelative(-2.5f, -2.666f, -3.792f, -4.541f)
                quadToRelative(-1.291f, -1.875f, -1.666f, -3.313f)
                quadToRelative(-0.375f, -1.437f, -0.375f, -3.187f)
                quadToRelative(0f, -3.792f, 2.645f, -6.459f)
                quadToRelative(2.646f, -2.666f, 6.48f, -2.666f)
                quadToRelative(1.833f, 0f, 3.562f, 0.687f)
                quadToRelative(1.729f, 0.688f, 3.063f, 1.979f)
                lineToRelative(-2.125f, 7.375f)
                quadToRelative(-0.167f, 0.625f, 0.229f, 1.125f)
                reflectiveQuadToRelative(1.062f, 0.5f)
                horizontalLineToRelative(3.334f)
                lineToRelative(-1.375f, 11.25f)
                quadToRelative(-0.042f, 0.375f, 0.271f, 0.417f)
                quadToRelative(0.312f, 0.042f, 0.395f, -0.292f)
                lineToRelative(3.459f, -11.458f)
                quadToRelative(0.208f, -0.625f, -0.188f, -1.167f)
                quadToRelative(-0.396f, -0.541f, -1.062f, -0.541f)
                horizontalLineToRelative(-3.334f)
                lineToRelative(2.917f, -8.709f)
                quadToRelative(1.042f, -0.583f, 2.167f, -0.875f)
                quadToRelative(1.125f, -0.291f, 2.333f, -0.291f)
                quadToRelative(3.792f, 0f, 6.437f, 2.666f)
                quadToRelative(2.646f, 2.667f, 2.646f, 6.459f)
                quadToRelative(0f, 1.708f, -0.396f, 3.166f)
                quadToRelative(-0.395f, 1.459f, -1.687f, 3.355f)
                quadToRelative(-1.292f, 1.895f, -3.771f, 4.562f)
                quadToRelative(-2.479f, 2.667f, -6.604f, 6.792f)
                quadToRelative(-1.667f, 1.625f, -3.958f, 1.625f)
                quadToRelative(-2.292f, 0f, -3.959f, -1.625f)
                close()
                moveTo(6.167f, 14.167f)
                quadToRelative(0f, 1.333f, 0.437f, 2.583f)
                quadToRelative(0.438f, 1.25f, 1.646f, 2.896f)
                reflectiveQuadToRelative(3.333f, 3.958f)
                quadToRelative(2.125f, 2.313f, 5.459f, 5.729f)
                quadToRelative(0.166f, 0.167f, 0.354f, 0.105f)
                quadToRelative(0.187f, -0.063f, 0.229f, -0.271f)
                lineToRelative(1.167f, -9.792f)
                horizontalLineToRelative(-0.75f)
                quadToRelative(-1.75f, 0f, -2.938f, -1.292f)
                quadToRelative(-1.187f, -1.291f, -0.521f, -3.541f)
                lineToRelative(1.667f, -5.875f)
                quadToRelative(-0.833f, -0.417f, -1.75f, -0.688f)
                quadToRelative(-0.917f, -0.271f, -1.875f, -0.271f)
                quadToRelative(-2.667f, 0f, -4.563f, 1.875f)
                quadToRelative(-1.895f, 1.875f, -1.895f, 4.584f)
                close()
                moveToRelative(27.666f, 0f)
                quadToRelative(0f, -2.709f, -1.896f, -4.584f)
                quadToRelative(-1.895f, -1.875f, -4.562f, -1.875f)
                quadToRelative(-0.625f, 0f, -1.208f, 0.105f)
                quadToRelative(-0.584f, 0.104f, -1.125f, 0.312f)
                lineToRelative(-1.375f, 4.167f)
                horizontalLineToRelative(0.125f)
                quadToRelative(1.75f, 0f, 2.833f, 1.396f)
                quadToRelative(1.083f, 1.395f, 0.417f, 3.479f)
                lineToRelative(-2.917f, 9.791f)
                quadToRelative(-0.083f, 0.209f, 0.146f, 0.354f)
                quadToRelative(0.229f, 0.146f, 0.437f, -0.062f)
                quadToRelative(2.75f, -2.708f, 4.521f, -4.625f)
                quadTo(31f, 20.708f, 32.021f, 19.25f)
                reflectiveQuadToRelative(1.417f, -2.646f)
                quadToRelative(0.395f, -1.187f, 0.395f, -2.437f)
                close()
                moveToRelative(-6.791f, 3f)
                close()
                moveToRelative(-12.459f, -2.625f)
                close()
            }
        }.build()
    }
}

@Composable
fun dislikeFilled(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "heart_broken",
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
                moveTo(16.083f, 32.042f)
                quadToRelative(-4.208f, -4.167f, -6.708f, -6.834f)
                quadToRelative(-2.5f, -2.666f, -3.792f, -4.541f)
                quadToRelative(-1.291f, -1.875f, -1.666f, -3.313f)
                quadToRelative(-0.375f, -1.437f, -0.375f, -3.187f)
                quadToRelative(0f, -3.792f, 2.645f, -6.459f)
                quadToRelative(2.646f, -2.666f, 6.438f, -2.666f)
                quadToRelative(1.875f, 0f, 3.604f, 0.687f)
                quadToRelative(1.729f, 0.688f, 3.063f, 1.979f)
                lineToRelative(-2.125f, 7.375f)
                quadToRelative(-0.167f, 0.625f, 0.229f, 1.125f)
                reflectiveQuadToRelative(1.062f, 0.5f)
                horizontalLineToRelative(3.334f)
                lineTo(20.417f, 28f)
                quadToRelative(-0.042f, 0.333f, 0.271f, 0.375f)
                quadToRelative(0.312f, 0.042f, 0.395f, -0.25f)
                lineToRelative(3.459f, -11.5f)
                quadToRelative(0.208f, -0.625f, -0.188f, -1.167f)
                quadToRelative(-0.396f, -0.541f, -1.062f, -0.541f)
                horizontalLineToRelative(-3.334f)
                lineToRelative(2.917f, -8.709f)
                quadToRelative(1.042f, -0.583f, 2.167f, -0.875f)
                quadToRelative(1.125f, -0.291f, 2.333f, -0.291f)
                quadToRelative(3.792f, 0f, 6.437f, 2.666f)
                quadToRelative(2.646f, 2.667f, 2.646f, 6.459f)
                quadToRelative(0f, 1.708f, -0.396f, 3.166f)
                quadToRelative(-0.395f, 1.459f, -1.687f, 3.355f)
                quadToRelative(-1.292f, 1.895f, -3.771f, 4.562f)
                quadToRelative(-2.479f, 2.667f, -6.604f, 6.792f)
                quadToRelative(-1.625f, 1.625f, -3.938f, 1.625f)
                quadToRelative(-2.312f, 0f, -3.979f, -1.625f)
                close()
            }
        }.build()
    }
}