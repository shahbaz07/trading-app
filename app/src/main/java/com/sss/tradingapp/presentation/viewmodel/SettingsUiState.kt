package com.sss.tradingapp.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.model.AppTheme

@Immutable
data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: AppLanguage = AppLanguage.SYSTEM
)
