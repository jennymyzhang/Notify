package com.example.notify.Services.UploadService

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.notify.Services.ImageToTextService.TextDetection.detectText
import com.example.notify.Services.PdfToImageService.PdfToImageConverter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storageMetadata
import java.io.ByteArrayOutputStream
import javax.inject.Inject

fun logBitmapAsBase64(bitmap: Bitmap) {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

    Log.d("BitmapAsBase64", base64String)
}

class FileUploadImpl @Inject constructor (
    private val storageReference: StorageReference,
    private var databaseReference: DatabaseReference,
)
    :FileUpload {
    override suspend fun uploadPdfFileToFirebase(
        toastGenerated: MutableState<Boolean>,
        msg: MutableState<String>,
        uploadProgress: MutableState<Float>,
        fileName: String?,
        pdfFileUri: Uri?,
        context: Context,
        subject: String,
        courseNum: String,
        term: String,
        year: String,
        uid: String,
        uuid: String,
    ) {
        var firstPageByteArray = ByteArray(0)

        val pdfToImageConverter = PdfToImageConverter()
        var detectedTexts = ""

        var metadata = storageMetadata {
            setCustomMetadata("uid", uid)
            contentType = "application/pdf"
            setCustomMetadata("subject", subject)
            setCustomMetadata("courseNum", courseNum)
            setCustomMetadata("term", term)
            setCustomMetadata("year", year)
            setCustomMetadata("uuid", uuid)
        }

        pdfFileUri?.let { uri ->
            val bitmaps = pdfToImageConverter.convertPdfToImages(uri, context)
            var firstPageImageBase64 = ""
            if (bitmaps.isNotEmpty()) {
                val firstPageBitmap = bitmaps.first()
                firstPageImageBase64 = bitmapToBase64(firstPageBitmap)
                // Now you have the first page's image encoded as a Base64 string
            }

            if (bitmaps.size == 1) {
                val text = detectText(context, bitmaps[0])
                Log.d("extracted text solo: ", text)
                detectedTexts += text
            } else {
                bitmaps.forEachIndexed { index, bitmap ->
                    logBitmapAsBase64(bitmap)
//                if(index == 0) {
//                    Log.d("page num, ", index.toString())
//                }
                    val text = detectText(context, bitmap)
                    Log.d("extracted text: ", text)
                    detectedTexts += text
                }
            }

            Log.d("detected texts", detectedTexts)
            val mStorageRef = storageReference.child("/pdfs/$subject$courseNum/$uid/$fileName")
            val fdbRef = databaseReference.child("/pdfs")
            mStorageRef.putFile(uri, metadata).addOnSuccessListener {
                mStorageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    fdbRef.push().key?.let { pushKey ->
                        val pdfFile = PdfFile(fileName.orEmpty(), downloadUri.toString(), uid, subject, courseNum, term, year, 0, 0, uuid, pushKey, detectedTexts, firstPageImageBase64)
                        // store user uploaded files in newly constructed user db
                        val userPostReference = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("user_posts").child(pushKey)
                        userPostReference.setValue(true).addOnSuccessListener {
                            Log.d("FirebaseService", "Post posted by user $uid with pushKey $pushKey")
                        }.addOnFailureListener { exception ->
                            Log.w("FirebaseService", "Error setting posted for user $uid with pushKey $pushKey", exception)
                        }
                        fdbRef.child(pushKey).setValue(pdfFile)
                            .addOnSuccessListener {
                                toastGenerated.value = true
                                msg.value = "Uploaded Successfully"
                                Log.i("upload", "Success! uploaded file: $fileName to realtime db")
                            }.addOnFailureListener { err ->
                                toastGenerated.value = true
                                msg.value = err.message.toString()
                                Log.e("upload", "Failed to uploaded file: $fileName to realtime db")
                            }
                    }
                }
            }.addOnProgressListener { uploadTask ->
                uploadProgress.value = (uploadTask.bytesTransferred * 100 / uploadTask.totalByteCount).toFloat()
                Log.i("upload", "Uploaded progress ${uploadProgress.value}")
            }.addOnFailureListener { err ->
                toastGenerated.value = true
                msg.value = err.message.toString()
                Log.e("upload", "Failed to upload $fileName to storage")
            }
        }
    }
    fun retrieveAllPdfFiles(callback: PdfFilesRetrievalCallback) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("pdfs")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<PdfFile>()
                snapshot.children.forEach { childSnapshot ->
                    val pdfFile = childSnapshot.getValue(PdfFile::class.java)
                    pdfFile?.let {
                        tempList.add(it)
                    }
                }
                if (tempList.isEmpty()) {
                    Log.e("retrieving", "No Data Found")
                    callback.onError("No Data Found")
                } else {
                    callback.onSuccess(tempList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("retrieving", "Error retrieving data", error.toException())
                callback.onError(error.toException().message ?: "Error retrieving data")
            }
        })
    }
    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
}

