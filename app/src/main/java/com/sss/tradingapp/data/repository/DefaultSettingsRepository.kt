package com.sss.tradingapp.data.repository

import com.sss.tradingapp.data.local.SettingsDataStore
import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.model.AppTheme
import com.sss.tradingapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSettingsRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {

    override val themeFlow: Flow<AppTheme?>
        get() = settingsDataStore.themeFlow

    override val languageFlow: Flow<AppLanguage?>
        get() = settingsDataStore.languageFlow

    override suspend fun setTheme(theme: AppTheme) {
        settingsDataStore.setTheme(theme)
    }

    override suspend fun setLanguage(language: AppLanguage) {
        settingsDataStore.setLanguage(language)
    }
}
