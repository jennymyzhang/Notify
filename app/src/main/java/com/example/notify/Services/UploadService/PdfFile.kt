package com.example.notify.Services.UploadService

data class PdfFile(
    val fileName: String, val downloadUrl: String, val uid: String,
    val subject: String, val courseNum: String, val term: String, val year: String, val likes: Int, val collects: Int, val uuid: String, val pushKey: String, val extractedText: String, val firstPageImageBase64: String
) {
    constructor() : this("", "", "","","","","",0, 0, "", "", "", "")
}