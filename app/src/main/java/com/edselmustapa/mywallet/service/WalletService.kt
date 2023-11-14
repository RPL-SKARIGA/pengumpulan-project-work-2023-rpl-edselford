package com.edselmustapa.mywallet.service

import android.util.Log
import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class WalletService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val repo: WalletRepo = retrofit.create(WalletRepo::class.java)

    suspend fun getWallet(email: String): Wallet {
        return try {
            val result = repo.get(WalletRequest(email = email))[0]
            Log.d("RES", listOf(result._id, result.user_id, result.wallet).toString())
            result
        } catch (e: Exception) {
            throw e
        }
    }
}

interface WalletRepo {
    @POST("wallet")
    suspend fun get(@Body walletRequest: WalletRequest): List<Wallet>
}

data class WalletRequest(
    val action: String = "getWallet",
    val email: String
)

data class Wallet(
    val _id: String?,
    val user_id: String,
    val wallet: Long
)