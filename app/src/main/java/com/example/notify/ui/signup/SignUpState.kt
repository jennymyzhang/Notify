package com.example.notify.ui.signup

data class SignUpState (
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)