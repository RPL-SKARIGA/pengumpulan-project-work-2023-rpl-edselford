package com.edselmustapa.mywallet.auth

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GithubAuth {
    private val provider = OAuthProvider.newBuilder("github.com")
    fun signIn(context: Context, onSuccess: (userData: UserData) -> Unit) {
        val pendingResultTask = Firebase.auth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    println("success")
                }
                .addOnFailureListener {
                    println("failure")
                }
        } else {
            Firebase.auth.startActivityForSignInWithProvider(context as Activity, provider.build())
                .addOnSuccessListener {
                    it.user?.run {
                        UserData(
                            userId = uid,
                            username = displayName ?: "",
                            email = email ?: "",
                            profilePictureUrl = photoUrl?.toString(),
                            userDbId = null,
                            name = null
                        )
                    }?.let { it1 -> onSuccess(it1) }
                }
                .addOnFailureListener {
                    it.printStackTrace()

                    Toast.makeText(
                        context,
                        "Error - " + it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}
