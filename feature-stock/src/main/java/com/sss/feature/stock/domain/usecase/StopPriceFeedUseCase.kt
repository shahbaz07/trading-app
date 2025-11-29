package com.sss.feature.stock.domain.usecase

import com.sss.feature.stock.domain.repository.StockRepository
import javax.inject.Inject

class StopPriceFeedUseCase @Inject constructor(
    private val repository: StockRepository
) {
    operator fun invoke() = repository.stopPriceFeed()
}
