package com.edselmustapa.mywallet.auth

import com.google.firebase.auth.FirebaseUser

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
) {
    companion object {
        fun fromFirebase(user: FirebaseUser?): UserData? {
            if (user == null) return null
            return UserData(
                userId = user.uid,
                userDbId = null,
                name = null,
                username = user.displayName ?: "",
                email = user.email ?: "",
                profilePictureUrl = user.photoUrl.toString()
            )
        }
    }
}
