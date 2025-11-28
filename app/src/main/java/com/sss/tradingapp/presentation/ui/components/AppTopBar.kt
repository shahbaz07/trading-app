package com.sss.tradingapp.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.sss.tradingapp.data.local.AppLanguage
import com.sss.tradingapp.data.local.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentTheme: AppTheme?,
    currentLanguage: AppLanguage?,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showThemeSubmenu by remember { mutableStateOf(false) }
    var showLanguageSubmenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(R.string.settings)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = {
                        showMenu = false
                        showThemeSubmenu = false
                        showLanguageSubmenu = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.language),
                                fontWeight = FontWeight.Medium
                            )
                        },
                        onClick = {
                            showLanguageSubmenu = !showLanguageSubmenu
                            showThemeSubmenu = false
                        }
                    )

                    if (showLanguageSubmenu) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "    ${stringResource(R.string.system_default)}",
                                    color = if (currentLanguage == null)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onLanguageChange(AppLanguage.SYSTEM)
                                showMenu = false
                                showLanguageSubmenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "    ${stringResource(R.string.english)}",
                                    color = if (currentLanguage == AppLanguage.ENGLISH)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onLanguageChange(AppLanguage.ENGLISH)
                                showMenu = false
                                showLanguageSubmenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "    ${stringResource(R.string.arabic)}",
                                    color = if (currentLanguage == AppLanguage.ARABIC)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onLanguageChange(AppLanguage.ARABIC)
                                showMenu = false
                                showLanguageSubmenu = false
                            }
                        )
                    }

                    HorizontalDivider()

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.theme),
                                fontWeight = FontWeight.Medium
                            )
                        },
                        onClick = {
                            showThemeSubmenu = !showThemeSubmenu
                            showLanguageSubmenu = false
                        }
                    )

                    if (showThemeSubmenu) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "    ${stringResource(R.string.system_default)}",
                                    color = if (currentTheme == null || currentTheme == AppTheme.SYSTEM)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onThemeChange(AppTheme.SYSTEM)
                                showMenu = false
                                showThemeSubmenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "    ${stringResource(R.string.light_theme)}",
                                    color = if (currentTheme == AppTheme.LIGHT)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onThemeChange(AppTheme.LIGHT)
                                showMenu = false
                                showThemeSubmenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "    ${stringResource(R.string.dark_theme)}",
                                    color = if (currentTheme == AppTheme.DARK)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onThemeChange(AppTheme.DARK)
                                showMenu = false
                                showThemeSubmenu = false
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
