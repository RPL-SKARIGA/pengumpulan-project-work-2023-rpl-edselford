package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.Date

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

    suspend fun transaction(email: String) = repo.transaction(TransactionRequest(email))
}

interface TransferRepo {
    @POST("transfer")
    suspend fun transfer(@Body request: TransferRequest): TransferResponse

    @POST("transaction")
    suspend fun transaction(@Body request: TransactionRequest): List<Transaction>
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

data class TransactionRequest(
    val email: String
)

data class Transaction(
    val _id: String,
    val sender: String,
    val receiver: String,
    val type: String,
    val amount: Long,
    val success: Boolean,
    val date: Date,
    val message: String,
    val isSender: Boolean
) {
    override fun toString(): String {
        return "{$_id, $sender, $receiver, $type, $amount, $success, $date, $message}"
    }
}