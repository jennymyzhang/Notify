package com.example.notify.Services.UserService

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserImpl @Inject constructor (
    private val firebaseAuth: FirebaseAuth
): User {
    override fun getCurrentUserId() : String? {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            return firebaseUser.uid
        }
        return null
    }

    override fun getCurrentUserEmail() : String? {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            return firebaseUser.email
        }
        return null
    }
}