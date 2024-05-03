package com.example.notify.DependencyInjections

import com.example.notify.Services.AuthService.Authentication
import com.example.notify.Services.AuthService.AuthenticationImpl
import com.example.notify.Services.UploadService.FileUpload
import com.example.notify.Services.UploadService.FileUploadImpl
import com.example.notify.Services.UserService.User
import com.example.notify.Services.UserService.UserImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //make sure object lives as long as our application does
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesAuthImpl(firebaseAuth: FirebaseAuth, databaseReference: DatabaseReference): Authentication {
        return AuthenticationImpl(firebaseAuth, databaseReference)
    }
    @Provides
    @Singleton
    fun provideUserImpl(firebaseAuth: FirebaseAuth): User {
        return UserImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance().reference

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance().reference

    @Provides
    @Singleton
    fun providePdfUploadImpl(storageReference: StorageReference, databaseReference: DatabaseReference)
    : FileUpload {
        return FileUploadImpl(storageReference, databaseReference)
    }
}