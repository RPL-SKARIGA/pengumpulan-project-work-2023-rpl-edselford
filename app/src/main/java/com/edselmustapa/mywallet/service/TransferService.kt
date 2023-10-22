package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class TransferService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val repo: TransferRepo = retrofit.create(TransferRepo::class.java)

    suspend fun transfer(email: String, toId: String, amount: Number, message: String) =
        repo.transfer(
            TransferRequest(email, toId, amount, message)
        )
}

interface TransferRepo {
    @POST("transfer")
    suspend fun transfer(@Body request: TransferRequest): TransferResponse
}

data class TransferResponse(
    val success: Boolean,
    val transactionResult: String
)

data class TransferRequest(
    val email: String,
    val to_id: String,
    val amount: Number,
    val message: String
)