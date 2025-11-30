package com.sss.tradingapp.domain.repository

import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeFlow: Flow<AppTheme?>
    val languageFlow: Flow<AppLanguage?>
    suspend fun setTheme(theme: AppTheme)
    suspend fun setLanguage(language: AppLanguage)
}
