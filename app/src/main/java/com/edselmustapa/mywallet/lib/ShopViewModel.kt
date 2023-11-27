package com.edselmustapa.mywallet.lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.service.ShopService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel : ViewModel() {
    private val service = ShopService()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun pay(email: String, amount: Number, callback: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            service.pay(email, amount)
            _loading.value = false
            callback()
        }
    }
}