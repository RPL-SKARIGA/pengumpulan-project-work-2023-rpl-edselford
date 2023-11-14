package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class TopupService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val repo: TopupRepo = retrofit.create(TopupRepo::class.java)

    suspend fun topup(email: String, amount: Number) = repo.topup(TopUpRequest(email, amount))

}

private interface TopupRepo {
    @POST("topup")
    suspend fun topup(@Body request: TopUpRequest): TransferResponse
}

private data class TopUpRequest(
    val email: String,
    val amount: Number
)