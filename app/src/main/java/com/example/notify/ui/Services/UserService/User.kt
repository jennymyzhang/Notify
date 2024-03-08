package com.example.notify.Services.UserService

interface User {
    fun getCurrentUserId(): String?

    fun getCurrentUserEmail(): String?
}