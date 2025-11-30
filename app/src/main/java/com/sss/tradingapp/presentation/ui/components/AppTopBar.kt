package com.sss.tradingapp.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sss.tradingapp.R
import com.sss.tradingapp.domain.model.AppLanguage
import com.sss.tradingapp.domain.model.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentTheme: AppTheme,
    currentLanguage: AppLanguage,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            SettingsMenu(
                currentTheme = currentTheme,
                currentLanguage = currentLanguage,
                onThemeChange = onThemeChange,
                onLanguageChange = onLanguageChange
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun SettingsMenu(
    currentTheme: AppTheme,
    currentLanguage: AppLanguage,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showThemeSubmenu by remember { mutableStateOf(false) }
    var showLanguageSubmenu by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                showThemeSubmenu = false
                showLanguageSubmenu = false
            }
        ) {
            LanguageSection(
                currentLanguage = currentLanguage,
                expanded = showLanguageSubmenu,
                onToggle = {
                    showLanguageSubmenu = !showLanguageSubmenu
                    showThemeSubmenu = false
                },
                onLanguageSelected = { language ->
                    onLanguageChange(language)
                    expanded = false
                    showLanguageSubmenu = false
                }
            )

            HorizontalDivider()

            ThemeSection(
                currentTheme = currentTheme,
                expanded = showThemeSubmenu,
                onToggle = {
                    showThemeSubmenu = !showThemeSubmenu
                    showLanguageSubmenu = false
                },
                onThemeSelected = { theme ->
                    onThemeChange(theme)
                    expanded = false
                    showThemeSubmenu = false
                }
            )
        }
    }
}

@Composable
private fun LanguageSection(
    currentLanguage: AppLanguage,
    expanded: Boolean,
    onToggle: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    MenuHeader(
        text = stringResource(R.string.language),
        onClick = onToggle
    )

    if (expanded) {
        SubmenuItem(
            text = stringResource(R.string.system_default),
            isSelected = currentLanguage == AppLanguage.SYSTEM,
            onClick = { onLanguageSelected(AppLanguage.SYSTEM) }
        )
        SubmenuItem(
            text = stringResource(R.string.english),
            isSelected = currentLanguage == AppLanguage.ENGLISH,
            onClick = { onLanguageSelected(AppLanguage.ENGLISH) }
        )
        SubmenuItem(
            text = stringResource(R.string.arabic),
            isSelected = currentLanguage == AppLanguage.ARABIC,
            onClick = { onLanguageSelected(AppLanguage.ARABIC) }
        )
    }
}

@Composable
private fun ThemeSection(
    currentTheme: AppTheme,
    expanded: Boolean,
    onToggle: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit
) {
    MenuHeader(
        text = stringResource(R.string.theme),
        onClick = onToggle
    )

    if (expanded) {
        SubmenuItem(
            text = stringResource(R.string.system_default),
            isSelected = currentTheme == AppTheme.SYSTEM,
            onClick = { onThemeSelected(AppTheme.SYSTEM) }
        )
        SubmenuItem(
            text = stringResource(R.string.light_theme),
            isSelected = currentTheme == AppTheme.LIGHT,
            onClick = { onThemeSelected(AppTheme.LIGHT) }
        )
        SubmenuItem(
            text = stringResource(R.string.dark_theme),
            isSelected = currentTheme == AppTheme.DARK,
            onClick = { onThemeSelected(AppTheme.DARK) }
        )
    }
}

@Composable
private fun MenuHeader(
    text: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                fontWeight = FontWeight.Medium
            )
        },
        onClick = onClick
    )
}

@Composable
private fun SubmenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = "    $text",
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        },
        onClick = onClick
    )
}
