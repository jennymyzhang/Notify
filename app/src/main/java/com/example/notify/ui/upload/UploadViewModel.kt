package com.example.notify.ui.upload


import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.notify.Services.CourseService.subjectToCourseCodes
import com.example.notify.Services.UploadService.FileUpload
import com.example.notify.Services.UserService.User
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor (
    private val fileUpload: FileUpload,
    private val user: User,
    @ApplicationContext private val applicationContext: Context
): ViewModel() {
    var pdfFileUri: Uri? = null
    var fileName: String? = null

    fun getCourseCodes() : Map<String, List<String>> {
        return subjectToCourseCodes
    }

    suspend fun UploadPdfFileToFirebase(
        displayToast: MutableState<Boolean>,
        toastMsg: MutableState<String>,
        uploadProgress: MutableState<Float>,
        courseNum: String,
        subject: String,
        term: String,
        year: String
        ) {
        val myUuid = UUID.randomUUID()
        val myUuidAsString = myUuid.toString()
        val fileUUID = myUuidAsString + user.getCurrentUserId().orEmpty()
        fileUpload.uploadPdfFileToFirebase(displayToast, toastMsg, uploadProgress, fileName,pdfFileUri, applicationContext, subject, courseNum,
            term, year, user.getCurrentUserId().orEmpty(), fileUUID)
    }
}