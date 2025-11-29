package com.sss.feature.stock.data.repository

import com.sss.core.network.WebSocketState
import com.sss.feature.stock.data.remote.StockWebSocketService
import com.sss.feature.stock.domain.model.Stock
import com.sss.feature.stock.domain.repository.StockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val webSocketService: StockWebSocketService
) : StockRepository {

    private val stockMap = mutableMapOf<String, Stock>()
    private val _stocksState = MutableStateFlow<List<Stock>>(emptyList())

    override val stocks: Flow<List<Stock>>
        get() = webSocketService.priceUpdates
            .onEach { dto ->
                val existingStock = stockMap[dto.symbol]
                val updatedStock = Stock(
                    symbol = dto.symbol,
                    price = dto.price,
                    previousPrice = existingStock?.price
                )
                stockMap[dto.symbol] = updatedStock
                _stocksState.value = stockMap.values
                    .sortedByDescending { it.price }
                    .toList()
            }
            .map { _stocksState.value }

    override val connectionState: StateFlow<WebSocketState>
        get() = webSocketService.connectionState

    override fun startPriceFeed(scope: CoroutineScope) {
        stockMap.clear()
        _stocksState.value = emptyList()
        webSocketService.start(scope)
    }

    override fun stopPriceFeed() {
        webSocketService.stop()
    }
}
