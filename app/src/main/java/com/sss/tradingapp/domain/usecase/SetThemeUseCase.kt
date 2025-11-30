package com.sss.tradingapp.domain.usecase

import com.sss.tradingapp.domain.model.AppTheme
import com.sss.tradingapp.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(theme: AppTheme) = repository.setTheme(theme)
}
