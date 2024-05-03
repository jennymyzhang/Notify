package com.example.notify.Services.ImageToTextService

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString
import com.google.api.gax.core.FixedCredentialsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

object TextDetection {
    suspend fun detectText(context: Context, bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        try {
            val credentialsStream = context.assets.open("notify.json")
            val credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))

            val settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build()

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val imgBytes = ByteString.copyFrom(byteArrayOutputStream.toByteArray())
            Log.d("img bytes, ", imgBytes.toString())

            val image = Image.newBuilder().setContent(imgBytes).build()
            val textDetection = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()
            val request = AnnotateImageRequest.newBuilder()
                .addFeatures(textDetection)
                .setImage(image)
                .build()

            ImageAnnotatorClient.create(settings).use { vision ->
                val response = vision.batchAnnotateImages(listOf(request)).responsesList
                val detectedTexts = mutableListOf<String>()
                for (res in response) {
                    if (res.hasError()) {
                        throw IOException("error processing image with google cloud vision API")
                    }
                    detectedTexts.add(res.fullTextAnnotation.text)
                }
                return@use detectedTexts.joinToString(separator = "\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
