package com.edselmustapa.mywallet.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
