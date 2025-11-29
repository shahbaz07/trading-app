package com.sss.feature.stock.presentation.viewmodel

sealed class StockIntent {
    data object StartFeed : StockIntent()
    data object StopFeed : StockIntent()
    data object ToggleFeed : StockIntent()
}
