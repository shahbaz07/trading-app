package com.sss.feature.stock.domain.repository

import com.sss.core.network.WebSocketState
import com.sss.feature.stock.domain.model.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StockRepository {
    val stocks: Flow<List<Stock>>
    val connectionState: StateFlow<WebSocketState>

    fun startPriceFeed()
    fun stopPriceFeed()
}
