package com.sss.tradingapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    ARABIC("ar"),
    SYSTEM("system")
}

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val themeKey = stringPreferencesKey("theme")
    private val languageKey = stringPreferencesKey("language")

    val themeFlow: Flow<AppTheme?> = context.dataStore.data.map { preferences ->
        preferences[themeKey]?.let { value ->
            AppTheme.entries.find { it.name == value }
        }
    }

    val languageFlow: Flow<AppLanguage?> = context.dataStore.data.map { preferences ->
        preferences[languageKey]?.let { value ->
            AppLanguage.entries.find { it.code == value }
        }
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language.code
        }
    }
}
