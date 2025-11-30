package com.sss.feature.stock.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class StockTest {

    @Test
    fun `price change should be UP when current price is higher than previous`() {
        val stock = Stock(
            symbol = "AAPL",
            price = BigDecimal("175.50"),
            previousPrice = BigDecimal("170.00")
        )
        assertEquals(PriceChange.UP, stock.priceChange)
    }

    @Test
    fun `price change should be DOWN when current price is lower than previous`() {
        val stock = Stock(
            symbol = "AAPL",
            price = BigDecimal("165.00"),
            previousPrice = BigDecimal("170.00")
        )
        assertEquals(PriceChange.DOWN, stock.priceChange)
    }

    @Test
    fun `price change should be NEUTRAL when prices are equal`() {
        val stock = Stock(
            symbol = "AAPL",
            price = BigDecimal("170.00"),
            previousPrice = BigDecimal("170.00")
        )
        assertEquals(PriceChange.NEUTRAL, stock.priceChange)
    }

    @Test
    fun `price change should be NEUTRAL when no previous price`() {
        val stock = Stock(
            symbol = "AAPL",
            price = BigDecimal("170.00"),
            previousPrice = null
        )
        assertEquals(PriceChange.NEUTRAL, stock.priceChange)
    }

    @Test
    fun `stock should have correct symbol and price`() {
        val stock = Stock(
            symbol = "GOOG",
            price = BigDecimal("140.25")
        )
        assertEquals("GOOG", stock.symbol)
        assertEquals(BigDecimal("140.25"), stock.price)
    }
}
