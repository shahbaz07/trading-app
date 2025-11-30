package com.sss.tradingapp.domain.usecase

import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppLanguage?> = repository.languageFlow
}
