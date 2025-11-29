package com.sss.feature.stock.domain.usecase

import com.sss.feature.stock.domain.model.Stock
import com.sss.feature.stock.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStockPricesUseCase @Inject constructor(
    private val repository: StockRepository
) {
    operator fun invoke(): Flow<List<Stock>> = repository.stocks
}
