package com.example.notify.ui.signup


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notify.Services.AuthService.Authentication
import com.example.notify.Services.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor (
    private val auth: Authentication
): ViewModel()
{
    val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun registerUser(email:String, password:String, firstName: String, lastName: String) = viewModelScope.launch {
        auth.registerUser(email, password, firstName, lastName).collect{
                result -> when(result) {
            is Resource.Success -> {
                _signUpState.send(SignUpState(isSuccess = "Sign In Success"))
            }
            is Resource.Loading ->{
                _signUpState.send(SignUpState(isLoading = true))
            }
            is Resource.Error ->{
                _signUpState.send(SignUpState(isError = result.message))
            }
        }
        }
    }
}