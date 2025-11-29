package com.sss.feature.stock.presentation.viewmodel

import app.cash.turbine.test
import com.sss.core.network.WebSocketState
import com.sss.feature.stock.domain.model.PriceChange
import com.sss.feature.stock.domain.model.Stock
import com.sss.feature.stock.domain.usecase.GetConnectionStateUseCase
import com.sss.feature.stock.domain.usecase.GetStockPricesUseCase
import com.sss.feature.stock.domain.usecase.StartPriceFeedUseCase
import com.sss.feature.stock.domain.usecase.StopPriceFeedUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getStockPricesUseCase: GetStockPricesUseCase
    private lateinit var startPriceFeedUseCase: StartPriceFeedUseCase
    private lateinit var stopPriceFeedUseCase: StopPriceFeedUseCase
    private lateinit var getConnectionStateUseCase: GetConnectionStateUseCase

    private val connectionStateFlow = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected())

    private lateinit var viewModel: StockViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getStockPricesUseCase = mockk()
        startPriceFeedUseCase = mockk(relaxed = true)
        stopPriceFeedUseCase = mockk(relaxed = true)
        getConnectionStateUseCase = mockk()

        every { getConnectionStateUseCase() } returns connectionStateFlow
        every { getStockPricesUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = StockViewModel(
            getStockPricesUseCase = getStockPricesUseCase,
            startPriceFeedUseCase = startPriceFeedUseCase,
            stopPriceFeedUseCase = stopPriceFeedUseCase,
            getConnectionStateUseCase = getConnectionStateUseCase
        )
    }

    @Test
    fun `initial state should be disconnected and feed inactive`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isConnected)
        assertFalse(state.isFeedActive)
        assertFalse(state.isLoading)
        assertTrue(state.stocks.isEmpty())
    }

    @Test
    fun `start feed should update state to feed active`() = runTest {
        val stocks = listOf(
            Stock("AAPL", 175.50),
            Stock("GOOG", 140.25)
        )
        every { getStockPricesUseCase() } returns flowOf(stocks)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processIntent(StockIntent.StartFeed)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isFeedActive)
        verify { startPriceFeedUseCase(any()) }
    }

    @Test
    fun `stop feed should update state to feed inactive`() = runTest {
        every { getStockPricesUseCase() } returns flowOf(emptyList())

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processIntent(StockIntent.StartFeed)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processIntent(StockIntent.StopFeed)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isFeedActive)
        verify { stopPriceFeedUseCase() }
    }

    @Test
    fun `toggle feed should start if inactive`() = runTest {
        every { getStockPricesUseCase() } returns flowOf(emptyList())

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processIntent(StockIntent.ToggleFeed)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isFeedActive)
    }

    @Test
    fun `toggle feed should stop if active`() = runTest {
        every { getStockPricesUseCase() } returns flowOf(emptyList())

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processIntent(StockIntent.StartFeed)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processIntent(StockIntent.ToggleFeed)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFeedActive)
    }

    @Test
    fun `connection state changes should update ui state`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        connectionStateFlow.value = WebSocketState.Connected
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isConnected)

        connectionStateFlow.value = WebSocketState.Disconnected("test")
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isConnected)
    }

    @Test
    fun `stock updates should be reflected in ui state`() = runTest {
        val stocks = listOf(
            Stock("AAPL", 175.50, 170.00),
            Stock("GOOG", 140.25, 145.00)
        )
        every { getStockPricesUseCase() } returns flowOf(stocks)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processIntent(StockIntent.StartFeed)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.stocks.size)
        assertEquals("AAPL", state.stocks[0].symbol)
        assertEquals(175.50, state.stocks[0].price, 0.01)
    }
}
