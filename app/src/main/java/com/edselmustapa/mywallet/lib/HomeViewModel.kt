package com.edselmustapa.mywallet.lib

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.service.Wallet
import com.edselmustapa.mywallet.service.WalletService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(val context: Context) : ViewModel() {
    private val walletService = WalletService()
    private val user = Firebase.auth.currentUser

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _wallet = MutableStateFlow(Wallet(null, "", 0))
    val wallet = _wallet.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        if (user != null) {
            viewModelScope.launch {
                _loading.value = true
                _wallet.value = try {
                    walletService.getWallet(user.email ?: "")
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    Wallet("", "", 0)
                }
                delay(1000)
                _loading.value = false
            }
        }
    }

}