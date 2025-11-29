package com.sss.feature.stock.domain.model

data class Stock(
    val symbol: String,
    val price: Double,
    val previousPrice: Double? = null
) {
    val priceChange: PriceChange
        get() = when {
            previousPrice == null -> PriceChange.NEUTRAL
            price > previousPrice -> PriceChange.UP
            price < previousPrice -> PriceChange.DOWN
            else -> PriceChange.NEUTRAL
        }
}

enum class PriceChange {
    UP, DOWN, NEUTRAL
}
