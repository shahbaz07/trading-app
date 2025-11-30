package com.sss.feature.stock.data.repository

import app.cash.turbine.test
import com.sss.core.network.WebSocketState
import com.sss.feature.stock.data.model.StockPriceDto
import com.sss.feature.stock.data.remote.StockService
import com.sss.feature.stock.domain.model.PriceChange
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultStockRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var stockService: StockService
    private lateinit var repository: DefaultStockRepository

    private val priceUpdatesFlow = MutableSharedFlow<StockPriceDto>()
    private val connectionStateFlow = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        stockService = mockk(relaxed = true)
        every { stockService.priceUpdates } returns priceUpdatesFlow
        every { stockService.connectionState } returns connectionStateFlow

        repository = DefaultStockRepository(stockService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `stocks flow should emit sorted list on price update`() = runTest {
        repository.stocks.test {
            priceUpdatesFlow.emit(StockPriceDto("AAPL", 175.50))
            val firstUpdate = awaitItem()
            assertEquals(1, firstUpdate.size)
            assertEquals("AAPL", firstUpdate[0].symbol)

            priceUpdatesFlow.emit(StockPriceDto("GOOG", 200.00))
            val secondUpdate = awaitItem()
            assertEquals(2, secondUpdate.size)
            assertEquals("GOOG", secondUpdate[0].symbol)
            assertEquals("AAPL", secondUpdate[1].symbol)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `stocks should track price changes`() = runTest {
        repository.stocks.test {
            priceUpdatesFlow.emit(StockPriceDto("AAPL", 170.00))
            val firstUpdate = awaitItem()
            assertEquals(PriceChange.NEUTRAL, firstUpdate[0].priceChange)

            priceUpdatesFlow.emit(StockPriceDto("AAPL", 175.00))
            val secondUpdate = awaitItem()
            assertEquals(PriceChange.UP, secondUpdate[0].priceChange)
            assertEquals(BigDecimal("170.0"), secondUpdate[0].previousPrice)

            priceUpdatesFlow.emit(StockPriceDto("AAPL", 165.00))
            val thirdUpdate = awaitItem()
            assertEquals(PriceChange.DOWN, thirdUpdate[0].priceChange)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `startPriceFeed should call stockService start`() = runTest {
        repository.startPriceFeed()
        verify { stockService.start() }
    }

    @Test
    fun `stopPriceFeed should call stockService stop`() = runTest {
        repository.stopPriceFeed()
        verify { stockService.stop() }
    }

    @Test
    fun `connectionState should expose stockService state`() = runTest {
        connectionStateFlow.value = WebSocketState.Connected
        assertEquals(WebSocketState.Connected, repository.connectionState.value)

        connectionStateFlow.value = WebSocketState.Disconnected("test")
        assertTrue(repository.connectionState.value is WebSocketState.Disconnected)
    }
}
