package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

//    suspend fun users(): List<User> = repo.users(UserRequest(action = "allUser"))

    suspend fun search(keyword: String): List<User> =
        repo.searchByName(keyword)
}

interface UserRepo {
    @POST("createAccount")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("user")
    suspend fun user(@Body request: UserRequest): UserResponse

    @GET("users/searchbyname")
    suspend fun searchByName(@Query("keyword") keyword: String): List<User>

}

data class RegisterRequest(
    val username: String,
    val name: String,
    val email: String,

    )

data class RegisterResponse(
    val acknowledged: Boolean,
    val insertedId: String,
    val msg: String
)

data class UserRequest(
    val action: String = "",

    val email: String = "",
    val keyword: String = ""
)

data class UserResponse(
    val user_id: String,
    val image: String,
    val username: String
)

data class User(
    val _id: String,
    val name: String,
    val email: String,
    val image: String,
    val username: String
)