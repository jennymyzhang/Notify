package com.example.notify.Services.AuthService

import com.example.notify.Services.Utils.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
interface Authentication {
    fun loginUser(email: String, password:String) : Flow<Resource<AuthResult>>
    fun registerUser(email:String, password: String, firstName: String, lastName: String): Flow<Resource<AuthResult>>
}