package com.mukund.bookcompanion.ui.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
val SYSTEM_THEME = booleanPreferencesKey(name = "follow_system")

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    var hasUserDarkThemeEnabled by mutableStateOf(false)
        private set
    var followSystemTheme by mutableStateOf(false)
        private set
    init {
        readThemePreferences()
    }
    private fun readThemePreferences() {
        viewModelScope.launch {
            dataStore.data
                .map { preferences ->
                    val darkTheme = preferences[DARK_THEME_KEY] ?: false
                    val systemTheme = preferences[SYSTEM_THEME] ?: false
                    darkTheme to systemTheme
                }.collect { (darkThemeValue, systemThemeValue) ->
                    hasUserDarkThemeEnabled = darkThemeValue
                    followSystemTheme = systemThemeValue
                }
        }
    }
    fun saveUserDarkThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[DARK_THEME_KEY] = enabled
            }
        }
    }
    fun saveUserFollowSystemEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SYSTEM_THEME] = enabled
            }
        }
    }
}
