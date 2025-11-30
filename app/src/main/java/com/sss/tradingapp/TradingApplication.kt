package com.sss.tradingapp

import android.app.Application
import com.sss.tradingapp.data.local.SettingsDataStore
import com.sss.tradingapp.domain.model.AppTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@HiltAndroidApp
class TradingApplication : Application() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SettingsEntryPoint {
        fun settingsDataStore(): SettingsDataStore
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        applyStoredTheme()
    }

    private fun applyStoredTheme() {
        val entryPoint = EntryPointAccessors.fromApplication(this, SettingsEntryPoint::class.java)
        val theme = runBlocking { entryPoint.settingsDataStore().themeFlow.first() } ?: AppTheme.SYSTEM
        theme.applyOnStartup(this)
    }
}
