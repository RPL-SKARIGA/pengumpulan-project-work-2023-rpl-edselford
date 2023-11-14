package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.Date

class ActivityService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val repo: ActivityRepo = retrofit.create(ActivityRepo::class.java)

    suspend fun activities(action: String = "getActivity", email: String) =
        repo.activities(ActivityRequest(action, email))

}


private interface ActivityRepo {
    @POST("activities")
    suspend fun activities(@Body request: ActivityRequest): List<UserActivity>
}

private data class ActivityRequest(
    val action: String = "getAcitivty",
    val email: String
)

data class UserActivity(
    val _id: String,
    val user_id: String,
    val activity: String,
    val date: Date
)