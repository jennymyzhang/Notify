package com.example.notify.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notify.Services.AuthService.Authentication
import com.example.notify.Services.UserService.User
import com.example.notify.Services.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor (
    private val auth: Authentication,
    private val user: User
): ViewModel()
{
    val _logInState = Channel<LogInState>()
    val logInState = _logInState.receiveAsFlow()

    fun loginUser(email:String, password:String) = viewModelScope.launch {
        auth.loginUser(email,password).collect{
            result -> when(result) {
                is Resource.Success -> {
                    _logInState.send(LogInState(isSuccess = "Sign In Success "))
                    Log.i("user", "current user uid: " + user.getCurrentUserId().toString() +
                    "\n email: " + user.getCurrentUserEmail().toString())
                }
                is Resource.Loading ->{
                    _logInState.send(LogInState(isLoading = true))
                }
                is Resource.Error ->{
                    _logInState.send(LogInState(isError = result.message))
                }
            }
        }
    }
}