package com.edselmustapa.mywallet.lib

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.service.ActivityService
import com.edselmustapa.mywallet.service.UserActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {
    private val service = ActivityService()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _activities = MutableStateFlow(emptyList<UserActivity>())
    val activities = _activities.asStateFlow()

    init {
        Firebase.auth.currentUser?.email?.let { getActivities(it) }
    }

    fun getActivities(
        email: String,
        onError: (e: Exception) -> Unit = {
            it.printStackTrace()
        }
    ) {
        viewModelScope.launch {
            _loading.value = true
            _activities.value = try {
                service.activities(email = email)
            } catch (e: Exception) {
                onError(e)
                emptyList<UserActivity>()
            }
            _loading.value = false
        }
    }
}

