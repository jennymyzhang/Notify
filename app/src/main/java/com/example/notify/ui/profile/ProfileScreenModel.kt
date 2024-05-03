package com.example.notify.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notify.Services.UploadService.PdfFile
import com.example.notify.Services.UploadService.PdfFilesRetrievalCallback
import com.example.notify.Services.fileRetrieve.fileInfo

class ProfileScreenModel: ViewModel() {
    private val infoRetrieve = fileInfo()
    private val _pdfFiles = MutableLiveData<List<PdfFile>>()
    private val _likedFiles = MutableLiveData<List<PdfFile>>()
    private val _collectedFiles = MutableLiveData<List<PdfFile>>()

    val pdfFiles: LiveData<List<PdfFile>> = _pdfFiles
    val likedFiles: LiveData<List<PdfFile>> = _likedFiles
    val collectedFiles: LiveData<List<PdfFile>> = _collectedFiles

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> = _fullName

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    // the intake will be the current user id, which returns all of the files that the user current liked
    fun retrieveUserPdfFiles(userId: String, like_or_collect: String) {
        if (like_or_collect == "likes") {
            infoRetrieve.retrieveUserCollectedPdfFiles(userId, like_or_collect, object : PdfFilesRetrievalCallback {
                override fun onSuccess(pdfFiles: List<PdfFile>) {
                    if (pdfFiles.isNotEmpty()) {
                        _message.postValue("Collected PDF files retrieved successfully!")
                        _likedFiles.postValue(pdfFiles)
                        // Optional: Log each retrieved PDF file name
                        pdfFiles.forEach { pdfFile ->
                            Log.d("YourViewModel", "Retrieved PDF File: ${pdfFile.fileName}")
                        }
                    } else {
                        _message.postValue("No collected PDF files found.")
                        Log.d("YourViewModel", "No collected PDF files found.")
                    }
                }

                override fun onError(errorMessage: String) {
                    _message.postValue("Error retrieving collected PDF files: $errorMessage")
                    Log.e("YourViewModel", "Error retrieving collected PDF files: $errorMessage")
                }
            })
        } else if (like_or_collect == "collects") {
            infoRetrieve.retrieveUserCollectedPdfFiles(userId, like_or_collect, object : PdfFilesRetrievalCallback {
                override fun onSuccess(pdfFiles: List<PdfFile>) {
                    if (pdfFiles.isNotEmpty()) {
                        _message.postValue("Collected PDF files retrieved successfully!")
                        _collectedFiles.postValue(pdfFiles)
                        // Optional: Log each retrieved PDF file name
                        pdfFiles.forEach { pdfFile ->
                            Log.d("YourViewModel", "Retrieved PDF File: ${pdfFile.fileName}")
                        }
                    } else {
                        _message.postValue("No collected PDF files found.")
                        Log.d("YourViewModel", "No collected PDF files found.")
                    }
                }

                override fun onError(errorMessage: String) {
                    _message.postValue("Error retrieving collected PDF files: $errorMessage")
                    Log.e("YourViewModel", "Error retrieving collected PDF files: $errorMessage")
                }
            })
        } else {
            infoRetrieve.retrieveUserCollectedPdfFiles(userId, like_or_collect, object : PdfFilesRetrievalCallback {
                override fun onSuccess(pdfFiles: List<PdfFile>) {
                    if (pdfFiles.isNotEmpty()) {
                        _message.postValue("Collected PDF files retrieved successfully!")
                        _pdfFiles.postValue(pdfFiles)
                        // Optional: Log each retrieved PDF file name
                        pdfFiles.forEach { pdfFile ->
                            Log.d("YourViewModel", "Retrieved PDF File: ${pdfFile.fileName}")
                        }
                    } else {
                        _message.postValue("No collected PDF files found.")
                        Log.d("YourViewModel", "No collected PDF files found.")
                    }
                }

                override fun onError(errorMessage: String) {
                    _message.postValue("Error retrieving collected PDF files: $errorMessage")
                    Log.e("YourViewModel", "Error retrieving collected PDF files: $errorMessage")
                }
            })
        }
    }
    fun fetchUserFullName(userId: String) {
        infoRetrieve.fetchUserFullName(userId) { fullName ->
            val nonNullFullName = fullName ?: "Unknown User"
            _fullName.postValue(nonNullFullName)
        }
    }
}