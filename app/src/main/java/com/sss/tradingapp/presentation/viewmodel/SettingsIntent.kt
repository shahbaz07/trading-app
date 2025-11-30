package com.sss.tradingapp.presentation.viewmodel

import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.model.AppTheme

sealed class SettingsIntent {
    data class ChangeTheme(val theme: AppTheme) : SettingsIntent()
    data class ChangeLanguage(val language: AppLanguage) : SettingsIntent()
}
