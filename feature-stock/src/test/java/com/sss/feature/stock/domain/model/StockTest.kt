package com.sss.feature.stock.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class StockTest {

    @Test
    fun `price change should be UP when current price is higher than previous`() {
        val stock = Stock(
            symbol = "AAPL",
            price = 175.50,
            previousPrice = 170.00
        )
        assertEquals(PriceChange.UP, stock.priceChange)
    }

    @Test
    fun `price change should be DOWN when current price is lower than previous`() {
        val stock = Stock(
            symbol = "AAPL",
            price = 165.00,
            previousPrice = 170.00
        )
        assertEquals(PriceChange.DOWN, stock.priceChange)
    }

    @Test
    fun `price change should be NEUTRAL when prices are equal`() {
        val stock = Stock(
            symbol = "AAPL",
            price = 170.00,
            previousPrice = 170.00
        )
        assertEquals(PriceChange.NEUTRAL, stock.priceChange)
    }

    @Test
    fun `price change should be NEUTRAL when no previous price`() {
        val stock = Stock(
            symbol = "AAPL",
            price = 170.00,
            previousPrice = null
        )
        assertEquals(PriceChange.NEUTRAL, stock.priceChange)
    }

    @Test
    fun `stock should have correct symbol and price`() {
        val stock = Stock(
            symbol = "GOOG",
            price = 140.25
        )
        assertEquals("GOOG", stock.symbol)
        assertEquals(140.25, stock.price, 0.001)
    }
}
