package com.sss.tradingapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.model.AppTheme
import com.sss.tradingapp.domain.usecase.GetLanguageUseCase
import com.sss.tradingapp.domain.usecase.GetThemeUseCase
import com.sss.tradingapp.domain.usecase.SetLanguageUseCase
import com.sss.tradingapp.domain.usecase.SetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    fun processIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ChangeTheme -> changeTheme(intent.theme)
            is SettingsIntent.ChangeLanguage -> changeLanguage(intent.language)
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            getThemeUseCase()
                .catch { e -> Timber.e(e, "Failed to observe theme") }
                .collect { theme ->
                    _uiState.update { it.copy(theme = theme ?: AppTheme.SYSTEM) }
                }
        }
        viewModelScope.launch {
            getLanguageUseCase()
                .catch { e -> Timber.e(e, "Failed to observe language") }
                .collect { language ->
                    _uiState.update { it.copy(language = language ?: AppLanguage.SYSTEM) }
                }
        }
    }

    private fun changeTheme(theme: AppTheme) {
        viewModelScope.launch {
            try {
                setThemeUseCase(theme)
            } catch (e: Exception) {
                Timber.e(e, "Failed to set theme")
            }
        }
    }

    private fun changeLanguage(language: AppLanguage) {
        viewModelScope.launch {
            try {
                setLanguageUseCase(language)
            } catch (e: Exception) {
                Timber.e(e, "Failed to set language")
            }
        }
    }
}
