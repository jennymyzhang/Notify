package com.example.notify.ui.search

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.notify.Services.UploadService.FileUploadImpl
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


@Composable
fun SearchScreen(onBackClick: () -> Unit, navController: NavHostController, currentUserId: String?) {
    val fileUploadService = remember {
        val storageReference = FirebaseStorage.getInstance().reference
        val databaseReference = FirebaseDatabase.getInstance().getReference("pdfs/MATH235")
        FileUploadImpl(storageReference, databaseReference)
    }

    // Create and remember the ViewModelFactory
    val searchModelFactory = remember { SearchModelFactory(fileUploadService) }

    // Retrieve the ViewModel using the factory
    val viewModel: SearchModel = viewModel(factory = searchModelFactory)

    val searchText by viewModel.searchText.collectAsState()
//    val notes by viewModel.notes.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val filteredPdfFiles by viewModel.filteredPdfFiles.collectAsState()
    Log.d("SearchScreen", "Filtered PDF Files: ${filteredPdfFiles.size}")
    Surface() {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(intrinsicSize = IntrinsicSize.Min)
            ) {
                Button(
                    onClick= onBackClick,
                    modifier=Modifier
                        .fillMaxHeight()
                        .width(50.dp),
                    contentPadding = PaddingValues(1.dp),
                    shape=RectangleShape
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                    )
                }
                TextField(
                    value = searchText,
                    onValueChange = viewModel::onSearchTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text="Search") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isSearching) {
                Box(modifier=Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier=Modifier.align(Alignment.Center)
                    )
                }
            } else if (filteredPdfFiles.isNotEmpty()) {
                NoteList(pdfFiles = filteredPdfFiles, navController = navController, currentUserId = currentUserId)
            } else {
                Text("No files found", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

