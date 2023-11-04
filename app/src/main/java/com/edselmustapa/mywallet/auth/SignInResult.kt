package com.edselmustapa.mywallet.auth

data class SignInResult (
    val data: UserData?,
    val errorMessage: String?
)

data class UserData (
    val userId: String,
    val userDbId: String?,
    val name: String?,
    val username: String,
    val email: String,
    val profilePictureUrl: String?
)
