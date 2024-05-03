package com.example.notify.Services.UploadService

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState

interface FileUpload {
    suspend fun uploadPdfFileToFirebase(toastGenerated: MutableState<Boolean>,
                                        toastMsg: MutableState<String>,
                                        uploadProgress: MutableState<Float>,
                                        fileName: String?,
                                        pdfFileUri: Uri?,
                                        context: Context,
                                        subject: String,
                                        courseNum: String,
                                        term: String,
                                        year: String,
                                        uid: String,
                                        uuid: String
    )
}

interface PdfFilesRetrievalCallback {
    fun onSuccess(pdfFiles: List<PdfFile>)
    fun onError(errorMessage: String)
}