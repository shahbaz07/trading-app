package com.sss.feature.stock.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StockPriceDto(
    val symbol: String,
    val price: Double
)
