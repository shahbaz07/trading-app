package com.sss.tradingapp.domain.usecase

import com.sss.tradingapp.domain.model.AppTheme
import com.sss.tradingapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppTheme?> = repository.themeFlow
}
