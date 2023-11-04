package com.edselmustapa.mywallet.config

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

data class Setting (
    val cardColor: String = "card_1"
)

sealed class SettingKey <T>(
    val key: Preferences.Key<T>
) {
    object CardColor : SettingKey<String>(key = stringPreferencesKey("card_color"))
}