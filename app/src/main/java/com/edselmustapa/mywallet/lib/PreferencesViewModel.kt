package com.edselmustapa.mywallet.lib

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.config.Setting
import com.edselmustapa.mywallet.config.SettingKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


class PreferencesViewModel(context: Context) : ViewModel() {
    private val store = context.dataStore

    companion object {
        val Context.dataStore by preferencesDataStore(name = "settings")
    }

    private val _setting = MutableStateFlow(Setting())
    val setting = _setting.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            store.data.catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }.map {
                Setting(cardColor = it[SettingKey.CardColor.key] ?: "card_1")
            }.collect {
                _setting.value = it
            }
        }
    }

    fun save(setting: Setting) {
        viewModelScope.launch {
            store.edit {
                it[SettingKey.CardColor.key] = setting.cardColor
            }
            refresh()
        }
    }

}