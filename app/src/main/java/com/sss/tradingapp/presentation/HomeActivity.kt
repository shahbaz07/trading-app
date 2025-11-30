package com.sss.tradingapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.presentation.ui.components.AppTopBar
import com.sss.tradingapp.presentation.ui.theme.TradingAppTheme
import com.sss.tradingapp.presentation.viewmodel.SettingsIntent
import com.sss.tradingapp.presentation.viewmodel.SettingsViewModel
import com.sss.feature.stock.presentation.ui.StockListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

            TradingAppTheme(appTheme = uiState.theme) {
                Scaffold(
                    topBar = {
                        AppTopBar(
                            currentTheme = uiState.theme,
                            currentLanguage = uiState.language,
                            onThemeChange = { theme ->
                                settingsViewModel.processIntent(SettingsIntent.ChangeTheme(theme))
                            },
                            onLanguageChange = { language ->
                                settingsViewModel.processIntent(SettingsIntent.ChangeLanguage(language))
                                applyLanguage(language)
                            }
                        )
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        StockListScreen()
                    }
                }
            }
        }
    }

    private fun applyLanguage(language: AppLanguage) {
        val localeList = when (language) {
            AppLanguage.ENGLISH -> LocaleListCompat.forLanguageTags("en")
            AppLanguage.ARABIC -> LocaleListCompat.forLanguageTags("ar")
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
