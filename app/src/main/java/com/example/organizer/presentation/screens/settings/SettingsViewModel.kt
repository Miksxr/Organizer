package com.example.organizer.presentation.screens.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    val themeState = dataStore.data.map {
        it[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ThemeMode.SYSTEM.name)

    val notificationsEnabled = dataStore.data.map {
        it[PreferencesKeys.NOTIFICATIONS] ?: true
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.THEME_MODE] = mode
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }

    private companion object {
        object PreferencesKeys {
            val THEME_MODE = stringPreferencesKey("theme_mode")
            val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
        }
    }
}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}