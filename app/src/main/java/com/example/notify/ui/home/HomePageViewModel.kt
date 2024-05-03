package com.example.notify.ui.homePage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notify.Services.UploadService.FileUploadImpl
import com.example.notify.Services.UploadService.PdfFile
import com.example.notify.Services.UploadService.PdfFilesRetrievalCallback
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class HomePageViewModel : ViewModel() {
    // Implement your ViewModel logic here
    // This might include LiveData for dynamic content on the HomePage,
    // functions to handle button clicks, etc.
    public val _message = MutableLiveData<String>()
    private val storageReference = FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().getReference("pdfs/MATH235")
    private val fileUploadService = FileUploadImpl(storageReference, databaseReference)
    private val _pdfFiles = MutableLiveData<List<PdfFile>>()
    val pdfFiles: LiveData<List<PdfFile>> = _pdfFiles

    fun retrievePdfFiles() {
        fileUploadService.retrieveAllPdfFiles(object : PdfFilesRetrievalCallback {
            override fun onSuccess(pdfFiles: List<PdfFile>) {
                if (pdfFiles.isNotEmpty()) {
                    _message.postValue("PDF files retrieved successfully!")
                    _pdfFiles.postValue(pdfFiles)
//                    pdfFiles.forEach { pdfFile ->
//                        Log.d("HomePageViewModel", "Retrieved PDF File: ${pdfFile.fileName}")
//                    }
                } else {
                    _message.postValue("No PDF files found.")
                    Log.d("HomePageViewModel", "No PDF files found.")
                }
            }
            override fun onError(errorMessage: String) {
                _message.postValue("Error retrieving PDF files: $errorMessage")
                Log.e("HomePageViewModel", "Error retrieving PDF files: $errorMessage")
            }
        })
    }
}
