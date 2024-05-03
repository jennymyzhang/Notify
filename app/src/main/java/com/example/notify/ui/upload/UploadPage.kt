package com.example.notify.ui.upload

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notify.R
import kotlinx.coroutines.launch


@Composable
fun UploadScreen( //refactor padding
    navController : NavController,
    viewModel: UploadViewModel = hiltViewModel()
) {
    val (title, onTitleChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (subject, setSubject) = rememberSaveable {
        mutableStateOf("")
    }
    val (term, setTerm) = rememberSaveable {
        mutableStateOf("")
    }
    val (year, setYear) = rememberSaveable {
        mutableStateOf("")
    }
    val (isTermExpanded, setIsTermExpanded) = rememberSaveable {
        mutableStateOf(false)
    }
    val (isYearExpanded, setIsYearExpanded) = rememberSaveable {
        mutableStateOf(false)
    }
    val (courseNum, setCourseNum) = rememberSaveable {
        mutableStateOf("")
    }
    val (isSubjectExpanded, setIsSubjectExpanded) = rememberSaveable {
        mutableStateOf(false)
    }
    val (isNumExpanded, setIsNumExpanded) = rememberSaveable {
        mutableStateOf(false)
    }

    val courseCodes = viewModel.getCourseCodes()

    val displayToast = remember {mutableStateOf(false)}
    val toastMsg = remember {mutableStateOf("")}
    val uploadProgress = remember{mutableStateOf(0.toFloat())}

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pdfUri = remember {mutableStateOf<Uri>(Uri.EMPTY)}
    val fileName = remember{mutableStateOf<String?>(null)}
    val selectPdfActivity = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { fileUri ->
        if(fileUri !== null) {
            pdfUri.value = fileUri
            fileName.value = DocumentFile.fromSingleUri(context, pdfUri.value)?.getName()
        }
        Log.i("upload", "uploaded file: $fileUri")
    }

    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.DarkGray
    val shapeColor = if (isSystemInDarkTheme()) Color.White else colorResource(R.color.extraLightGray)
    Column(modifier = Modifier.fillMaxSize())
    {
        Box() {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.33f),
                painter = painterResource(id = R.drawable.shape),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier
                        .clickable { navController.navigate("home") }
                        .size(40.dp),// Add some space between the search icon and the user icon
                    tint = uiColor
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        )
        {
            Divider(color = shapeColor, thickness = 1.dp, modifier = Modifier.padding(start = 15.dp, end=15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

            )
            {
                Row(modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_attach_file_24),
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "edit",
                        colorFilter = ColorFilter.tint(uiColor)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "Upload a File",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text=fileName.value.orEmpty(),
                        maxLines = 1,
                        color = uiColor,
                        overflow = TextOverflow.Ellipsis)
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                scope.launch {
                                    selectPdfActivity.launch(arrayOf("application/pdf"))
                                }
                            },
                        contentDescription = "add",
                        tint = uiColor
                    )

                    Spacer(modifier = Modifier.width(15.dp))
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Divider(color = shapeColor, thickness = 2.dp, modifier = Modifier.padding(start = 15.dp, end=15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                )
            {
                Row(modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_menu_book_24),
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "course",
                        colorFilter = ColorFilter.tint(uiColor)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "Course Subject",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = subject,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold)
                    Column(modifier = Modifier.padding(end = 15.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "back",
                            modifier = Modifier
                                .clickable { navController.navigate("home") }
                                .size(40.dp)
                                .clickable {
                                    setIsSubjectExpanded(!isSubjectExpanded)
                                },
                            tint = uiColor,
                            )

                        DropdownMenu(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxHeight(0.3f),
                            expanded = isSubjectExpanded,
                            onDismissRequest = { setIsSubjectExpanded(false) }) {
                            courseCodes.keys.forEach { subject ->
                                DropdownMenuItem(
                                    text = {Text(text = subject, color = Color.Black)},
                                    onClick = {
                                        setSubject(subject)
                                        setIsSubjectExpanded(false)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Divider(color = shapeColor, thickness = 2.dp, modifier = Modifier.padding(start = 15.dp, end=15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                )
            {
                Row(modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_format_list_numbered_24),
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "course",
                        colorFilter = ColorFilter.tint(uiColor)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "Course Number",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = courseNum,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold)
                    Column(modifier = Modifier.padding(end = 15.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "back",
                            modifier = Modifier
                                .clickable { navController.navigate("home") }
                                .size(40.dp)
                                .clickable { setIsNumExpanded(!isNumExpanded) },
                            tint = uiColor,

                            )

                        DropdownMenu(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxHeight(0.3f),
                            expanded = isNumExpanded,
                            onDismissRequest = { setIsNumExpanded(false) }) {
                            courseCodes[subject]?.forEach { code ->
                                DropdownMenuItem(
                                    text = { Text(text = code, color = Color.Black) },
                                    onClick = {
                                        setCourseNum(code)
                                        setIsNumExpanded(false)
                                })
                            }
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(10.dp))

            Divider(color = shapeColor, thickness = 2.dp, modifier = Modifier.padding(start = 15.dp, end=15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                )
            {
                Row(modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_face_2_24),
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "term",
                        colorFilter = ColorFilter.tint(uiColor)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "Term",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = term,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold)

                    Column(modifier = Modifier.padding(end = 15.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "back",
                            modifier = Modifier
                                .clickable { navController.navigate("home") }
                                .size(40.dp)
                                .clickable { setIsTermExpanded(!isTermExpanded) },
                            tint = uiColor,

                            )

                        DropdownMenu(
                            modifier = Modifier.background(Color.LightGray),
                            expanded = isTermExpanded,
                            onDismissRequest = { setIsTermExpanded(false) }) {
                            DropdownMenuItem(
                                text = { Text(text = "F", color = Color.Black) },
                                onClick = {
                                    setTerm("F")
                                    setIsTermExpanded(false)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "W", color = Color.Black) },
                                onClick = {
                                    setTerm("W")
                                    setIsTermExpanded(false)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "S", color = Color.Black) },
                                onClick = {
                                    setTerm("S")
                                    setIsTermExpanded(false)
                                }
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(15.dp))

            Divider(color = shapeColor, thickness = 2.dp, modifier = Modifier.padding(start = 15.dp, end=15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                )
            {
                Row(
                    modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "calendar",
                        colorFilter = ColorFilter.tint(uiColor)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "Year",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = year,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold
                    )
                    Column(modifier = Modifier.padding(end = 15.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "back",
                            modifier = Modifier
                                .clickable { navController.navigate("home") }
                                .size(40.dp)
                                .clickable { setIsYearExpanded(!isYearExpanded) },
                            tint = uiColor,

                            )

                        DropdownMenu(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxHeight(0.3f),
                            expanded = isYearExpanded,
                            onDismissRequest = { setIsYearExpanded(false) }) {
                            for (i in 2024 downTo 2000) {
                                DropdownMenuItem(
                                    text = { Text(text = i.toString(), color = Color.Black) },
                                    onClick = {
                                        setYear(i.toString())
                                        setIsYearExpanded(false)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            if (uploadProgress.value != 0.0000.toFloat()) {
                LinearProgressIndicator(
                    progress = { uploadProgress.value.toFloat() },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    color = uiColor,
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center,
                ) {
                Button(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
                    border = BorderStroke(1.dp,uiColor),
                    shape = RoundedCornerShape(60.dp),
                    onClick = {
                              scope.launch {
                                  if (subject.isEmpty() || term.isEmpty() ||
                                      year.isEmpty() || courseNum.isEmpty()) {
                                      Toast.makeText(
                                          context,
                                          "Please fill in all required fields",
                                          Toast.LENGTH_SHORT
                                      ).show()
                                  } else {
                                      if  (pdfUri.value != Uri.EMPTY) {
                                          viewModel.pdfFileUri = pdfUri.value
                                          viewModel.fileName = fileName.value
                                          viewModel.UploadPdfFileToFirebase(
                                              displayToast,
                                              toastMsg,
                                              uploadProgress,
                                              courseNum,
                                              subject,
                                              term,
                                              year
                                          )
                                      } else {
                                          Toast.makeText(
                                              context,
                                              "Please select pdf first",
                                              Toast.LENGTH_SHORT
                                          ).show()
                                      }
                                  }
                              }
                    },
                ){
                    Text(
                        text = "POST",
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiColor,
                        fontWeight = FontWeight.Bold)
                }
            }
            if(displayToast.value) {
                Toast.makeText(context, toastMsg.value, Toast.LENGTH_SHORT).show()
                displayToast.value = false
                toastMsg.value = ""
                uploadProgress.value = 0.toFloat()
                pdfUri.value = Uri.EMPTY
                fileName.value = null
                setSubject("")
                setCourseNum("")
                setTerm("")
                setYear("")

                navController.navigate("Home")
            }

        }
    }
}



