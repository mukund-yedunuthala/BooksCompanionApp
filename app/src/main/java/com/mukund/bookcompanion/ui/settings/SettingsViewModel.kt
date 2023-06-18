package com.mukund.bookcompanion.ui.settings
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
val SYSTEM_THEME = booleanPreferencesKey(name = "follow_system")

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val  dataStore: DataStore<Preferences>
) : ViewModel() {


    var hasUserDarkThemeEnabled by mutableStateOf(false)
        private set
    var followSystemTheme by mutableStateOf(true)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        dataStore.data.map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }.onEach { value ->
            hasUserDarkThemeEnabled = value
        }.launchIn(viewModelScope)
        dataStore.data.map { preferences ->
            preferences[SYSTEM_THEME] ?: true
        }.onEach { value ->
            followSystemTheme = value
        }.launchIn(viewModelScope)
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
