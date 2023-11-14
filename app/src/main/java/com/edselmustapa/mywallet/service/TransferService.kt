package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.Date
import kotlin.math.floor

class TransferService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val repo: TransferRepo = retrofit.create(TransferRepo::class.java)

    suspend fun transfer(
        email: String,
        receipentEmail: String,
        total: Long,
        fee: Long = floor(total.toDouble() / 100).toLong()
    ) {
        println(listOf(email, receipentEmail, total,  fee, total + fee, "Transfer"))
        repo.transfer(
            TransferRequest(email, receipentEmail, total,  fee, total + fee, "Transfer")
        )

    }

    suspend fun transaction(email: String) = repo.transaction(TransactionRequest(email))
}

interface TransferRepo {
    @POST("paymentgate")
    suspend fun transfer(@Body request: TransferRequest): TransferResponse

    @POST("transaction/all")
    suspend fun transaction(@Body request: TransactionRequest): List<Transaction>
}

data class TransferResponse(
    val success: Boolean,
    val transactionResult: String
)

data class TransferRequest(
    val email: String,
    val receipent_email: String,
    val total: Number,
    val fee: Number,
    val subtotal: Number,
    val payment_type: String
)

data class TransactionRequest(
    val email: String
)

data class Transaction(
    val _id: String,
    val receipent_id: String,
    val payment_type: String,
    val total_transaction: Long,
    val fee: Long,
    val subtotal: Long,
    val date: Date,
    val is_sender: Boolean
)