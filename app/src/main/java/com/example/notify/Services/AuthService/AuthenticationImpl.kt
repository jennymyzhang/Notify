package com.example.notify.Services.AuthService

import android.util.Log
import com.example.notify.Services.Utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationImpl @Inject constructor (
        private val firebaseAuth: FirebaseAuth,
        private var databaseReference: DatabaseReference,
)
    : Authentication {
    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun registerUser(email: String, password: String, firstName: String, lastName:String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            storeUserInDB(firstName, lastName, result.user?.uid.orEmpty())
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    private fun storeUserInDB(firstName: String, lastName:String, uid: String) {
        try {
            val path = databaseReference.child("users")
            val userData = UserData(firstName, lastName, uid)

            // Push the user data object into the "users" collection using the UID as the key
            path.child(uid).setValue(userData)
                    .addOnSuccessListener {
                        // Data push successful
                        Log.i("user","User data successfully pushed to the database.")
                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors
                        Log.e("user", "Error pushing user data: ${exception.message}")
                    }
        }catch(e: Exception){
            Log.e("user", "Error pushing user data: ${e.message}")
        }
    }
}