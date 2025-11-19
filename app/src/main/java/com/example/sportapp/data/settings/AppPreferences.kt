package com.example.sportapp.data.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DS_NAME = "app_prefs"
val Context.dataStore by preferencesDataStore(DS_NAME)

enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** Identifiants Guardian qu'on utilise déjà : football, sport, world, uk, technology */
data class UserPrefs(
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val defaultSectionId: String = "football",
    val languageTag: String = "fr"
)

private object Keys {
    val THEME = intPreferencesKey("theme_mode")
    val DEFAULT_SECTION = stringPreferencesKey("default_section")
    val LANGUAGE = stringPreferencesKey("language_tag")
}

class AppPreferences(private val context: Context) {
    val prefsFlow: Flow<UserPrefs> = context.dataStore.data.map { p ->
        UserPrefs(
            theme = ThemeMode.entries.getOrElse(p[Keys.THEME] ?: 0) { ThemeMode.SYSTEM },
            defaultSectionId = p[Keys.DEFAULT_SECTION] ?: "football",
            languageTag = p[Keys.LANGUAGE] ?: "fr"
        )
    }

    suspend fun setTheme(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME] = mode.ordinal }
    }

    suspend fun setDefaultSection(sectionId: String)  {
        context.dataStore.edit { it[Keys.DEFAULT_SECTION] = sectionId }
    }

    suspend fun setLanguage(tag: String) {
        context.dataStore.edit { it[Keys.LANGUAGE] = tag }
        // Applique immédiatement
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }
}