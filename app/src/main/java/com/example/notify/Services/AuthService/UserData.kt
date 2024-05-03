package com.example.notify.Services.AuthService

data class UserData(val firstName: String, val lastName: String, val uid: String) {
    constructor() : this("", "", "")
}