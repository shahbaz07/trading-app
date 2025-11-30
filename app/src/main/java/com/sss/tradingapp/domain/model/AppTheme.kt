package com.sss.tradingapp.domain.model

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

enum class AppTheme {
    LIGHT, DARK, SYSTEM;

    /**
     * Apply theme at app startup (Application.onCreate).
     * Uses UiModeManager on API 31+ for proper initial configuration.
     */
    fun applyOnStartup(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            uiModeManager.setApplicationNightMode(
                when (this) {
                    LIGHT -> UiModeManager.MODE_NIGHT_NO
                    DARK -> UiModeManager.MODE_NIGHT_YES
                    SYSTEM -> UiModeManager.MODE_NIGHT_AUTO
                }
            )
        } else {
            apply()
        }
    }

    /**
     * Apply theme at runtime (when user changes theme).
     * Uses AppCompatDelegate to avoid activity recreation flicker.
     */
    fun apply() {
        AppCompatDelegate.setDefaultNightMode(
            when (this) {
                LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                DARK -> AppCompatDelegate.MODE_NIGHT_YES
                SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }
}
