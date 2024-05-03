package com.example.notify.ui.note
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.notify.R
import com.example.notify.databinding.PdfViewBinding
import com.example.notify.ui.profile.ProfileScreenModel
import com.example.notify.ui.theme.Black
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

@Composable
fun NoteScreen(id: String, downloadUrl: String, pushKey: String, userId: String, fileName: String, navController: NavHostController) {
    // this save to collects
    val noteScreenModel: NoteScreenModel = viewModel()
    val profileScreenModel: ProfileScreenModel = viewModel()
    var input: InputStream? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit){
        withContext(Dispatchers.IO) {
            val temp = URL(downloadUrl).openStream()
            input = temp
        }
    }
    input?.let {
        PdfView(downloadUrl = it, pushKey = pushKey, id = id, navController=navController,
            noteScreenModel=noteScreenModel, profileScreenModel=profileScreenModel, userId = userId, fileName = fileName)
    }
}

@Composable
fun PdfView(
    downloadUrl: InputStream,
    pushKey: String,
    id: String,
    navController: NavHostController,
    noteScreenModel: NoteScreenModel,
    profileScreenModel: ProfileScreenModel,
    userId: String,
    fileName: String
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
            topBar = { TopSection(navController = navController, id = id, currentUserId = userId,
                pushKey=pushKey, noteScreenModel=noteScreenModel) },
            bottomBar = { Bottom (modifier = Modifier.fillMaxWidth(), pushKey, id, userId, noteScreenModel, fileName) }
        ) { paddingValues ->
            Center(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .background(Color.Transparent),
                downloadUrl = downloadUrl
            )
        }
    }
}

@Composable
private fun Center(modifier: Modifier=Modifier, downloadUrl: InputStream?) {
    if (downloadUrl != null) {
        Box(modifier) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp),
                factory = { context ->
                    PdfViewBinding.inflate(LayoutInflater.from(context))
                        .apply {
                            pdfView.fromStream(downloadUrl).load()
                        }
                        .root
                },
            )
        }
    }
}


@Composable
private fun Bottom(modifier:Modifier = Modifier,
                   pushKey: String, id: String,
                   currentUserId: String, noteScreenModel:NoteScreenModel,
                   fileName: String) {
    val favorite by noteScreenModel.like.observeAsState(initial = 0)
    val collect by noteScreenModel.collect.observeAsState(initial = 0)
    noteScreenModel.fetchLikes(pushKey)
    noteScreenModel.fetchCollects(pushKey)
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black

    Column(modifier=modifier) {
        Row(modifier= Modifier
            .align(Alignment.Start)
            .padding(start = 10.dp),) {
            Text(fileName, maxLines=2)
        }
        Row(modifier=Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically) {
            FavoriteButton(
                addFavorite={
                    noteScreenModel.updateLikes(pushKey, currentUserId, true)
                    noteScreenModel.fetchLikes(pushKey)
                },
                subFavorite={
                    noteScreenModel.updateLikes(pushKey, currentUserId, false)
                    noteScreenModel.fetchLikes(pushKey)
                },
                color=uiColor,
                id = currentUserId,
                pushKey = pushKey
            )
            Text(favorite.toString(), color=uiColor, modifier=Modifier.padding(end=2.dp))
            CollectButton(
                addCollect={
                    noteScreenModel.updateCollects(pushKey, currentUserId, true)
                    noteScreenModel.fetchCollects(pushKey)
                },
                subCollect={
                    noteScreenModel.updateCollects(pushKey, currentUserId, false)
                    noteScreenModel.fetchCollects(pushKey)
                },
                color=uiColor,
                id = currentUserId,
                pushKey = pushKey
            )
            Text(collect.toString(), color=uiColor, modifier=Modifier.padding(end=20.dp))
        }
    }
}

@Composable
private fun TopSection(navController: NavHostController, id: String,
                       currentUserId: String, pushKey: String, noteScreenModel: NoteScreenModel) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Button(
                onClick= { navController.popBackStack() },
                modifier= Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .padding(end = 16.dp),
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
            Image(
                painter = painterResource(id = R.drawable.baseline_face_2_24),
                contentDescription = "Big Elephant",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.Black, CircleShape)
                    .clickable { navController.navigate("profile/$id/$currentUserId/posts") }
            )
            if (id == currentUserId) {
                Spacer(Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    onClick = {
                        noteScreenModel.deleteFiles(pushKey, id)
                        navController.navigate("home")
                    }
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    addFavorite: ()->Unit,
    subFavorite: ()->Unit,
    id: String,
    pushKey: String
) {
    val profileScreenModel: ProfileScreenModel = viewModel()
    var isFavorite by remember { mutableStateOf(false) }
    profileScreenModel.retrieveUserPdfFiles(id, "likes")
    val likedFiles by profileScreenModel.likedFiles.observeAsState(initial = emptyList())
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            likedFiles.forEach{pdfFile->
                Log.d("LikedFilesTest", pdfFile.pushKey)
            }
            run breaking@ {
                likedFiles.forEach{pdfFile ->
                    if (pdfFile.pushKey == pushKey) {
                        isFavorite = true
                        return@breaking
                    } else {
                        isFavorite = false
                    }
                }
            }
        }, 100)
    }

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
            tint = if (isFavorite) Color.Red else color,
            imageVector = if (isFavorite) Icons.Filled.Favorite
            else Icons.Default.FavoriteBorder,
            contentDescription = null
        )
    }

}

@Composable
fun CollectButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    addCollect: ()->Unit,
    subCollect: ()->Unit,
    id: String,
    pushKey: String
) {
    val profileScreenModel: ProfileScreenModel = viewModel()
    var isCollect by remember { mutableStateOf(false) }
    profileScreenModel.retrieveUserPdfFiles(id, "collects")
    val collectedFiles by profileScreenModel.collectedFiles.observeAsState(initial = emptyList())
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            run breaking@ {
                collectedFiles.forEach{pdfFile ->
                    if (pdfFile.pushKey == pushKey) {
                        isCollect = true
                        return@breaking
                    } else {
                        isCollect = false
                    }
                }
            }
        }, 100)
    }
    IconToggleButton(
        checked = isCollect,
        onCheckedChange = {
            if (isCollect) {
                subCollect()
            } else {
                addCollect()
            }
            isCollect = !isCollect
        }
    ) {
        Icon(
            tint = color,
            imageVector = if (isCollect) {
                filledFlag()
            } else {
                rememberFlag()
            },
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
    }
}
@Composable
fun filledFlag(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "flag",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.667f, 35.458f)
                quadToRelative(-0.75f, 0f, -1.25f, -0.52f)
                quadToRelative(-0.5f, -0.521f, -0.5f, -1.23f)
                verticalLineTo(8f)
                quadToRelative(0f, -0.75f, 0.5f, -1.25f)
                reflectiveQuadToRelative(1.25f, -0.5f)
                horizontalLineToRelative(12.208f)
                quadToRelative(0.625f, 0f, 1.104f, 0.396f)
                quadToRelative(0.479f, 0.396f, 0.604f, 1.021f)
                lineToRelative(0.459f, 2.041f)
                horizontalLineTo(32f)
                quadToRelative(0.75f, 0f, 1.271f, 0.521f)
                quadToRelative(0.521f, 0.521f, 0.521f, 1.271f)
                verticalLineToRelative(13.25f)
                quadToRelative(0f, 0.75f, -0.521f, 1.25f)
                reflectiveQuadTo(32f, 26.5f)
                horizontalLineToRelative(-8.708f)
                quadToRelative(-0.625f, 0f, -1.104f, -0.396f)
                quadToRelative(-0.48f, -0.396f, -0.605f, -1.021f)
                lineToRelative(-0.458f, -2.041f)
                horizontalLineToRelative(-9.708f)
                verticalLineToRelative(10.666f)
                quadToRelative(0f, 0.709f, -0.521f, 1.23f)
                quadToRelative(-0.521f, 0.52f, -1.229f, 0.52f)
                close()
            }
        }.build()
    }
}
@Composable
fun rememberFlag(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "flag",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.667f, 35.458f)
                quadToRelative(-0.75f, 0f, -1.25f, -0.52f)
                quadToRelative(-0.5f, -0.521f, -0.5f, -1.23f)
                verticalLineTo(8f)
                quadToRelative(0f, -0.75f, 0.5f, -1.25f)
                reflectiveQuadToRelative(1.25f, -0.5f)
                horizontalLineToRelative(12.208f)
                quadToRelative(0.625f, 0f, 1.104f, 0.396f)
                quadToRelative(0.479f, 0.396f, 0.604f, 1.021f)
                lineToRelative(0.459f, 2.041f)
                horizontalLineTo(32f)
                quadToRelative(0.75f, 0f, 1.271f, 0.521f)
                quadToRelative(0.521f, 0.521f, 0.521f, 1.271f)
                verticalLineToRelative(13.25f)
                quadToRelative(0f, 0.75f, -0.521f, 1.25f)
                reflectiveQuadTo(32f, 26.5f)
                horizontalLineToRelative(-8.708f)
                quadToRelative(-0.625f, 0f, -1.104f, -0.375f)
                quadToRelative(-0.48f, -0.375f, -0.605f, -1.042f)
                lineToRelative(-0.458f, -2.041f)
                horizontalLineToRelative(-9.708f)
                verticalLineToRelative(10.666f)
                quadToRelative(0f, 0.709f, -0.521f, 1.23f)
                quadToRelative(-0.521f, 0.52f, -1.229f, 0.52f)
                close()
                moveToRelative(11.166f, -19.083f)
                close()
                moveToRelative(4f, 6.583f)
                horizontalLineToRelative(5.417f)
                verticalLineTo(13.25f)
                horizontalLineToRelative(-9.167f)
                lineToRelative(-0.75f, -3.458f)
                horizontalLineToRelative(-8.916f)
                verticalLineTo(19.5f)
                horizontalLineToRelative(12.666f)
                close()
            }
        }.build()
    }
}
