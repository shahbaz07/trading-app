package com.sss.feature.stock.data.repository

import app.cash.turbine.test
import com.sss.core.network.WebSocketState
import com.sss.feature.stock.data.model.StockPriceDto
import com.sss.feature.stock.data.remote.StockWebSocketService
import com.sss.feature.stock.domain.model.PriceChange
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
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

@OptIn(ExperimentalCoroutinesApi::class)
class StockRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var webSocketService: StockWebSocketService
    private lateinit var repository: StockRepositoryImpl

    private val priceUpdatesFlow = MutableSharedFlow<StockPriceDto>()
    private val connectionStateFlow = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        webSocketService = mockk(relaxed = true)
        every { webSocketService.priceUpdates } returns priceUpdatesFlow
        every { webSocketService.connectionState } returns connectionStateFlow

        repository = StockRepositoryImpl(webSocketService)
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
            assertEquals(170.00, secondUpdate[0].previousPrice)

            priceUpdatesFlow.emit(StockPriceDto("AAPL", 165.00))
            val thirdUpdate = awaitItem()
            assertEquals(PriceChange.DOWN, thirdUpdate[0].priceChange)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `startPriceFeed should call webSocketService start`() = runTest {
        val scope = CoroutineScope(testDispatcher)
        repository.startPriceFeed(scope)
        verify { webSocketService.start(scope) }
    }

    @Test
    fun `stopPriceFeed should call webSocketService stop`() = runTest {
        repository.stopPriceFeed()
        verify { webSocketService.stop() }
    }

    @Test
    fun `connectionState should expose webSocketService state`() = runTest {
        connectionStateFlow.value = WebSocketState.Connected
        assertEquals(WebSocketState.Connected, repository.connectionState.value)

        connectionStateFlow.value = WebSocketState.Disconnected("test")
        assertTrue(repository.connectionState.value is WebSocketState.Disconnected)
    }
}
