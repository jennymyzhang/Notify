package com.example.notify.Services.fileRetrieve

import android.util.Log
import com.example.notify.Services.UploadService.PdfFile
import com.example.notify.Services.UploadService.PdfFilesRetrievalCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class fileInfo {
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("pdfs")
    private val userDatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
    // use our pushKey as the child to find corresponding number of likes
    fun fetchLikesForPushKey(pushKey: String, callback: (Int?) -> Unit) {
        val specificReference = databaseReference.child(pushKey)
        specificReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val likes = dataSnapshot.child("likes").getValue(Int::class.java)
                callback(likes)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("FirebaseService", "fetchLikesForPushKey:onCancelled", databaseError.toException())
                callback(null)
            }
        })
    }
    fun fetchColletsForPushKey(pushKey: String, callback: (Int?) -> Unit) {
        val specificReference = databaseReference.child(pushKey)
        specificReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val collects = dataSnapshot.child("collects").getValue(Int::class.java)
                callback(collects)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("FirebaseService", "fetchLikesForPushKey:onCancelled", databaseError.toException())
                callback(null)
            }
        })
    }
    // update likes based on if we want to increment it or decrement it
    fun updateLikesBasedOnPushkey(pushKey: String, userId: String, increment: Boolean) {
        val userLikeDatabaseReference = userDatabaseReference.child(userId).child("user_likes").child(pushKey)
        if (increment) {
            // Increment like
            userLikeDatabaseReference.setValue(true).addOnSuccessListener {
                Log.d("FirebaseService", "Post liked by user $userId with pushKey $pushKey")
            }.addOnFailureListener { exception ->
                Log.w("FirebaseService", "Error setting like for user $userId with pushKey $pushKey", exception)
            }
            val specificReference = databaseReference.child(pushKey)
            specificReference.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentLikes = mutableData.child("likes").getValue(Int::class.java) ?: 0
                    mutableData.child("likes").value = currentLikes + 1
                    return Transaction.success(mutableData)
                }
                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (databaseError != null) {
                        Log.w("FirebaseService", "updateLikesForPushKey:onComplete:error", databaseError.toException())
                    } else if (committed) {
                        Log.d("FirebaseService", "Likes successfully incremented for $pushKey")
                    }
                }
            })
        } else {
            // Decrement like
            userLikeDatabaseReference.removeValue().addOnSuccessListener {
                Log.d("FirebaseService", "Post unliked by user $userId with pushKey $pushKey")
            }.addOnFailureListener { exception ->
                Log.w("FirebaseService", "Error removing like for user $userId with pushKey $pushKey", exception)
            }
            val specificReference = databaseReference.child(pushKey)
            specificReference.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentLikes = mutableData.child("likes").getValue(Int::class.java) ?: 0
                    mutableData.child("likes").value = (currentLikes - 1).coerceAtLeast(0) // Prevent negative likes
                    return Transaction.success(mutableData)
                }
                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (databaseError != null) {
                        Log.w("FirebaseService", "updateLikesForPushKey:onComplete:error", databaseError.toException())
                    } else if (committed) {
                        Log.d("FirebaseService", "Likes successfully decremented for $pushKey")
                    }
                }
            })
        }
    }
    // update collects based on boolean and pushKey
    fun updateCollectsBasedOnPushkey(pushKey: String, userId: String, collects: Boolean) {
        val userLikeDatabaseReference = userDatabaseReference.child(userId).child("user_collects").child(pushKey)
        if (collects) {
            // Increment like
            userLikeDatabaseReference.setValue(true).addOnSuccessListener {
                Log.d("FirebaseService", "Post liked by user $userId with pushKey $pushKey")
            }.addOnFailureListener { exception ->
                Log.w("FirebaseService", "Error setting like for user $userId with pushKey $pushKey", exception)
            }
            val specificReference = databaseReference.child(pushKey)
            specificReference.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentCollects = mutableData.child("collects").getValue(Int::class.java) ?: 0
                    mutableData.child("collects").value = currentCollects + 1
                    return Transaction.success(mutableData)
                }
                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (databaseError != null) {
                        Log.w("FirebaseService", "updateCollectsForPushKey:onComplete:error", databaseError.toException())
                    } else if (committed) {
                        Log.d("FirebaseService", "Collects successfully incremented for $pushKey")
                    }
                }
            })
        } else {
            // Decrement like
            userLikeDatabaseReference.removeValue().addOnSuccessListener {
                Log.d("FirebaseService", "Post unliked by user $userId with pushKey $pushKey")
            }.addOnFailureListener { exception ->
                Log.w("FirebaseService", "Error removing like for user $userId with pushKey $pushKey", exception)
            }
            val specificReference = databaseReference.child(pushKey)
            specificReference.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentCollects = mutableData.child("collects").getValue(Int::class.java) ?: 0
                    mutableData.child("collects").value = (currentCollects - 1).coerceAtLeast(0) // Prevent negative likes
                    return Transaction.success(mutableData)
                }
                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (databaseError != null) {
                        Log.w("FirebaseService", "updateCollectsForPushKey:onComplete:error", databaseError.toException())
                    } else if (committed) {
                        Log.d("FirebaseService", "Collects successfully decremented for $pushKey")
                    }
                }
            })
        }
    }
    // retrieve all collects files, the intake will be the current user id
    fun retrieveUserCollectedPdfFiles(userId: String, like_or_collect_or_post: String, callback: PdfFilesRetrievalCallback) {
        var userCollectsReference = userDatabaseReference.child(userId)
        when (like_or_collect_or_post) {
            "likes" -> userCollectsReference = userCollectsReference.child("user_likes")
            "posts" -> userCollectsReference = userCollectsReference.child("user_posts")
            else -> userCollectsReference = userCollectsReference.child("user_collects")
        }
        userCollectsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(userSnapshot: DataSnapshot) {
                val pushKeys = userSnapshot.children.mapNotNull { it.key }
                if (pushKeys.isEmpty()) {
                    Log.e("retrieving", "User has no $like_or_collect_or_post")
                    callback.onError("User has no $like_or_collect_or_post")
                    return
                }

                val tempList = mutableListOf<PdfFile>()
                val databaseReference = FirebaseDatabase.getInstance().reference.child("pdfs")
                var completedRequests = 0

                pushKeys.forEach { pushKey ->
                    databaseReference.child(pushKey).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(pdfSnapshot: DataSnapshot) {
                            if (pdfSnapshot.exists()) {
                                pdfSnapshot.getValue(PdfFile::class.java)?.let {
                                    tempList.add(it)
                                }
                            } else {
                                // If the pushKey does not correspond to an existing PDF, remove it from the user's collection
                                userCollectsReference.child(pushKey).removeValue().addOnSuccessListener {
                                    Log.d("Cleanup", "Removed orphaned reference for pushKey $pushKey from user's $like_or_collect_or_post")
                                }
                            }
                            completedRequests++
                            if (completedRequests == pushKeys.size) {
                                if (tempList.isEmpty()) {
                                    callback.onError("No matching PDF files found")
                                } else {
                                    callback.onSuccess(tempList)
                                }
                            }
                        }
                        override fun onCancelled(pdfError: DatabaseError) {
                            completedRequests++
                            if (completedRequests == pushKeys.size && tempList.isEmpty()) {
                                callback.onError("Error retrieving PDF files")
                            }
                        }
                    })
                }
            }
            override fun onCancelled(userError: DatabaseError) {
                callback.onError(userError.toException().message ?: "Error retrieving user $like_or_collect_or_post")
            }
        })
    }
    // delete files, argument takes in a push key
    fun deletePdfFile(pushKey: String, uid: String) {
        // Reference to the PDF file in the Realtime Database
        val pdfFileRef = FirebaseDatabase.getInstance().reference.child("pdfs").child(pushKey)
        // Delete the PDF file entry
        pdfFileRef.removeValue().addOnSuccessListener {
            Log.d("DeleteService", "Successfully deleted PDF file with pushKey $pushKey from Realtime Database")
            // If the file was also referenced under the user's data, delete that reference
            val userPostRef = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("user_posts").child(pushKey)
            userPostRef.removeValue().addOnSuccessListener {
                Log.d("DeleteService", "Successfully deleted reference to PDF file with pushKey $pushKey from user's posts")
            }.addOnFailureListener { exception ->
                Log.e("DeleteService", "Error deleting reference to PDF file with pushKey $pushKey from user's posts", exception)
            }
        }.addOnFailureListener { exception ->
            Log.e("DeleteService", "Error deleting PDF file with pushKey $pushKey from Realtime Database", exception)
        }
    }
    // retrieve userName
    fun fetchUserFullName(userId: String, callback: (String?) -> Unit) {
        val userRef = userDatabaseReference.child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val firstName = snapshot.child("firstName").getValue(String::class.java)
                val lastName = snapshot.child("lastName").getValue(String::class.java)
                if (firstName != null && lastName != null) {
                    // Concatenate first name and last name with a space in between
                    val fullName = "$firstName $lastName"
                    callback(fullName)
                } else {
                    Log.e("fileInfo", "Unable to fetch full name for user $userId")
                    callback(null)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("fileInfo", "fetchUserFullName:onCancelled", error.toException())
                callback(null)
            }
        })
    }



}
