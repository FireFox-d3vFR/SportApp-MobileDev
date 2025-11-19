package com.example.sportapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportapp.data.settings.AppPreferences
import com.example.sportapp.data.settings.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val defaultSectionId: String = "football",
    val language: String = "fr"
)

/** Liste des sections proposées dans l’app (id Guardian -> label UI) */
val SECTION_OPTIONS: List<Pair<String, String>> = listOf(
    "football" to "Football",
    "sport" to "Sport",
    "world" to "World",
    "uk" to "UK",
    "technology" to "Tech"
)

class SettingsViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = AppPreferences(app)

    val state = prefs.prefsFlow
        .map { SettingsUiState(it.theme, it.defaultSectionId, it.languageTag) }
        .stateIn(viewModelScope, SharingStarted.Lazily, SettingsUiState())

    fun setTheme(theme: ThemeMode) = viewModelScope.launch {
        prefs.setTheme(theme)
    }

    fun setDefaultSection(id: String) = viewModelScope.launch {
        prefs.setDefaultSection(id)
    }

    fun setLanguage(tag: String) = viewModelScope.launch {
        prefs.setLanguage(tag)
    }
}
