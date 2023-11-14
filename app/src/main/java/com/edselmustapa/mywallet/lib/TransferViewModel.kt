package com.edselmustapa.mywallet.lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.service.TransferService
import com.edselmustapa.mywallet.service.User
import com.edselmustapa.mywallet.service.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransferViewModel : ViewModel() {
    private val transferService = TransferService()
    private val userService = UserService()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _userSearch = MutableStateFlow(listOf<User>())
    val userSearch = _userSearch.asStateFlow()

    private val _searchLoading = MutableStateFlow(false)
    val searchLoading = _searchLoading.asStateFlow()

    fun transfer(
        email: String,
        receipentEmail: String,
        total: Long,
        callback: suspend () -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            transferService.transfer(email, receipentEmail, total)
            callback()
            _loading.value = false
        }
    }

    fun search(keyword: String) {
        if (keyword.isBlank()) {
            _userSearch.value = emptyList()
            return
        }
        viewModelScope.launch {
            _searchLoading.value = true
            _userSearch.value = try {
                userService.search(keyword)
            } catch(e: Exception) {
                emptyList()
            }
            _searchLoading.value = false
        }
    }
}