package com.edselmustapa.mywallet.lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.service.TopupService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopupViewModel : ViewModel() {
    private val service = TopupService()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun topup(amount: Number, email: String, callback: () -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            service.topup(email, amount)
            _loading.value = false
            callback()
        }
    }
}