package com.sss.tradingapp.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.sss.tradingapp.R
import com.sss.tradingapp.domain.model.AppTheme

@Composable
fun TradingAppTheme(
    appTheme: AppTheme? = null,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM, null -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colorResource(R.color.primary),
            onPrimary = colorResource(R.color.on_primary),
            background = colorResource(R.color.background),
            onBackground = colorResource(R.color.on_background),
            surface = colorResource(R.color.surface),
            onSurface = colorResource(R.color.on_surface),
            surfaceVariant = colorResource(R.color.topbar),
            onSurfaceVariant = colorResource(R.color.on_surface)
        )
    } else {
        lightColorScheme(
            primary = colorResource(R.color.primary),
            onPrimary = colorResource(R.color.on_primary),
            background = colorResource(R.color.background),
            onBackground = colorResource(R.color.on_background),
            surface = colorResource(R.color.surface),
            onSurface = colorResource(R.color.on_surface),
            surfaceVariant = colorResource(R.color.topbar),
            onSurfaceVariant = colorResource(R.color.on_surface)
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                window.statusBarColor = colorScheme.surfaceVariant.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
