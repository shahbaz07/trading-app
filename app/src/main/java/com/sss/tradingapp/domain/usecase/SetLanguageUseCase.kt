package com.sss.tradingapp.domain.usecase

import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.repository.SettingsRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(language: AppLanguage) = repository.setLanguage(language)
}
