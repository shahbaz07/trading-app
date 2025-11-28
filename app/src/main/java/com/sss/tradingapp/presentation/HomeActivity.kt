package com.sss.tradingapp.presentation

import android.content.res.Configuration
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
import com.sss.tradingapp.data.local.AppLanguage
import com.sss.tradingapp.data.local.AppTheme
import com.sss.tradingapp.presentation.ui.components.AppTopBar
import com.sss.tradingapp.presentation.ui.theme.TradingAppTheme
import com.sss.tradingapp.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentTheme by settingsViewModel.currentTheme.collectAsStateWithLifecycle()
            val currentLanguage by settingsViewModel.currentLanguage.collectAsStateWithLifecycle()

            val effectiveTheme = currentTheme ?: AppTheme.SYSTEM

            TradingAppTheme(appTheme = effectiveTheme) {
                Scaffold(
                    topBar = {
                        AppTopBar(
                            currentTheme = currentTheme,
                            currentLanguage = currentLanguage,
                            onThemeChange = { theme ->
                                settingsViewModel.setTheme(theme)
                            },
                            onLanguageChange = { language ->
                                settingsViewModel.setLanguage(language)
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
                        // PriceTrackerScreen will be added here
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

    private fun getSystemLanguage(): AppLanguage {
        val systemLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            resources.configuration.locale
        }
        return if (systemLocale.language == "ar") AppLanguage.ARABIC else AppLanguage.ENGLISH
    }
}
