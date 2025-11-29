package com.sss.feature.stock.presentation.viewmodel

import com.sss.feature.stock.domain.model.Stock

data class StockUiState(
    val stocks: List<Stock> = emptyList(),
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val isFeedActive: Boolean = false,
    val error: String? = null
)
