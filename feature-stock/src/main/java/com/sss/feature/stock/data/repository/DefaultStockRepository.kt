package com.sss.feature.stock.data.repository

import com.sss.core.network.WebSocketState
import com.sss.feature.stock.data.remote.StockService
import com.sss.feature.stock.domain.model.Stock
import com.sss.feature.stock.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultStockRepository @Inject constructor(
    private val stockService: StockService
) : StockRepository {

    override val stocks: Flow<List<Stock>>
        get() = stockService.priceUpdates
            .scan(emptyMap<String, Stock>()) { stockMap, dto ->
                val existingStock = stockMap[dto.symbol]
                val updatedStock = Stock(
                    symbol = dto.symbol,
                    price = dto.price.toBigDecimal(),
                    previousPrice = existingStock?.price
                )
                stockMap + (dto.symbol to updatedStock)
            }
            .drop(1)
            .map { stockMap ->
                stockMap.values.sortedByDescending { it.price }
            }

    override val connectionState: StateFlow<WebSocketState>
        get() = stockService.connectionState

    override fun startPriceFeed() {
        stockService.start()
    }

    override fun stopPriceFeed() {
        stockService.stop()
    }
}
