package com.example.organizer.presentation.screens.settings

import androidx.datastore.core.DataStore
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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
) : ViewModel() {

    val themeState = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.THEME_MODE] = mode
        }
    }

    private companion object {
        object PreferencesKeys {
            val THEME_MODE = stringPreferencesKey("theme_mode")
        }
    }
}
enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}