package com.sss.feature.stock.data.remote

import com.sss.core.network.WebSocketState
import com.sss.feature.stock.data.model.StockPriceDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StockService {
    val priceUpdates: Flow<StockPriceDto>
    val connectionState: StateFlow<WebSocketState>
    fun start()
    fun stop()
}
