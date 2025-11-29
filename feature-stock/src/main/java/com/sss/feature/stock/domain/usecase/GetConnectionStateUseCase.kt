package com.sss.feature.stock.domain.usecase

import com.sss.core.network.WebSocketState
import com.sss.feature.stock.domain.repository.StockRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetConnectionStateUseCase @Inject constructor(
    private val repository: StockRepository
) {
    operator fun invoke(): StateFlow<WebSocketState> = repository.connectionState
}
