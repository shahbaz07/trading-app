package com.sss.tradingapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.model.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val themeKey = stringPreferencesKey("theme")
    private val languageKey = stringPreferencesKey("language")

    val themeFlow: Flow<AppTheme?> = context.dataStore.data
        .catch { e ->
            if (e is IOException) {
                Timber.e(e, "Error reading theme preference")
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preferences ->
            preferences[themeKey]?.let { value ->
                AppTheme.entries.find { it.name == value }
            }
        }

    val languageFlow: Flow<AppLanguage?> = context.dataStore.data
        .catch { e ->
            if (e is IOException) {
                Timber.e(e, "Error reading language preference")
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preferences ->
            preferences[languageKey]?.let { value ->
                AppLanguage.entries.find { it.code == value }
            }
        }

    suspend fun setTheme(theme: AppTheme) {
        try {
            context.dataStore.edit { preferences ->
                preferences[themeKey] = theme.name
            }
        } catch (e: IOException) {
            Timber.e(e, "Error saving theme preference")
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        try {
            context.dataStore.edit { preferences ->
                preferences[languageKey] = language.code
            }
        } catch (e: IOException) {
            Timber.e(e, "Error saving language preference")
        }
    }
}
