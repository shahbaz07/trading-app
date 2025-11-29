package com.sss.feature.stock.domain.usecase

import com.sss.feature.stock.domain.repository.StockRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class StartPriceFeedUseCase @Inject constructor(
    private val repository: StockRepository
) {
    operator fun invoke(scope: CoroutineScope) = repository.startPriceFeed(scope)
}
