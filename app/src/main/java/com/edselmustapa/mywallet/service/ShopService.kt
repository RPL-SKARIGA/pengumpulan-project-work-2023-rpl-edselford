package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class ShopService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val repo: ShopRepo = retrofit.create(ShopRepo::class.java)

    suspend fun pay(email: String, amount: Number) = repo.pay(PayRequest(email, amount))
}


interface ShopRepo {
    @POST("paymentemp")
    suspend fun pay(@Body request: PayRequest): PayResult
}

data class PayRequest(
    val email: String,
    val amount: Number
)

data class PayResult(
    val success: Boolean,
    val transactionResult: String
)