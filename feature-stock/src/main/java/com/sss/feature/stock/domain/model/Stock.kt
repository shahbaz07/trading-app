package com.sss.feature.stock.domain.model

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

@Immutable
data class Stock(
    val symbol: String,
    val price: BigDecimal,
    val previousPrice: BigDecimal? = null
) {
    val priceChange: PriceChange
        get() = when {
            previousPrice == null -> PriceChange.NEUTRAL
            price > previousPrice -> PriceChange.UP
            price < previousPrice -> PriceChange.DOWN
            else -> PriceChange.NEUTRAL
        }
}
