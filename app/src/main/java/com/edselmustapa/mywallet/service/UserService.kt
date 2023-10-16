package com.edselmustapa.mywallet.service

import android.util.Log
import androidx.compose.ui.res.stringResource
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class UserService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val repo: UserRepo = retrofit.create(UserRepo::class.java)

    suspend fun register(username: String, email: String, name: String): RegisterResponse {
        return try {
            repo.register(RegisterRequest(username, name, email))
        } catch (e: Exception) {
            RegisterResponse(msg = e.message ?: "", acknowledged = false, insertedId = "")
        }
    }


    suspend fun user(email: String): UserResponse {
        return try {
            repo.user(UserRequest(email))
        } catch (e: Exception) {
            e.printStackTrace()
            UserResponse("", "", "")
        }
    }
}

interface UserRepo {
    @POST("createAccount")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("user")
    suspend fun user(@Body request: UserRequest): UserResponse
}

data class RegisterRequest(
    val username: String,
    val name: String,
    val email: String
)

data class RegisterResponse(
    val acknowledged: Boolean,
    val insertedId: String,
    val msg: String
)

data class UserRequest(
    val email: String
)

data class UserResponse(
    val user_id: String,
    val image: String,
    val username: String
)