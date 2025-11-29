package com.sss.feature.stock.data.remote

import com.sss.core.network.WebSocketClient
import com.sss.core.network.WebSocketState
import com.sss.feature.stock.data.model.StockPriceDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class StockWebSocketService @Inject constructor(
    private val webSocketClient: WebSocketClient
) {
    private val json = Json { ignoreUnknownKeys = true }

    private val _priceUpdates = MutableSharedFlow<StockPriceDto>(extraBufferCapacity = 64)
    val priceUpdates: Flow<StockPriceDto> = _priceUpdates.asSharedFlow()

    val connectionState: StateFlow<WebSocketState> = webSocketClient.state

    private var connectionJob: Job? = null
    private var priceGeneratorJob: Job? = null

    private val symbols = listOf(
        "AAPL", "GOOG", "TSLA", "AMZN", "MSFT", "NVDA", "META", "NFLX", "AMD", "INTC",
        "CRM", "ORCL", "CSCO", "ADBE", "PYPL", "UBER", "LYFT", "SNAP", "TWTR", "SPOT",
        "ZM", "SHOP", "SQ", "COIN", "HOOD"
    )

    private val basePrices = mapOf(
        "AAPL" to 175.0, "GOOG" to 140.0, "TSLA" to 250.0, "AMZN" to 180.0, "MSFT" to 380.0,
        "NVDA" to 480.0, "META" to 350.0, "NFLX" to 450.0, "AMD" to 120.0, "INTC" to 45.0,
        "CRM" to 250.0, "ORCL" to 115.0, "CSCO" to 50.0, "ADBE" to 550.0, "PYPL" to 65.0,
        "UBER" to 60.0, "LYFT" to 15.0, "SNAP" to 12.0, "TWTR" to 45.0, "SPOT" to 180.0,
        "ZM" to 70.0, "SHOP" to 75.0, "SQ" to 80.0, "COIN" to 150.0, "HOOD" to 12.0
    )

    fun start(scope: CoroutineScope) {
        if (connectionJob?.isActive == true) return

        connectionJob = scope.launch {
            webSocketClient.connect(WEBSOCKET_URL).collect { message ->
                try {
                    val stockPrice = json.decodeFromString<StockPriceDto>(message)
                    _priceUpdates.emit(stockPrice)
                } catch (e: Exception) {
                    // Log parsing error if needed
                }
            }
        }

        priceGeneratorJob = scope.launch {
            while (isActive) {
                if (webSocketClient.isConnected()) {
                    symbols.forEach { symbol ->
                        val basePrice = basePrices[symbol] ?: 100.0
                        val variance = basePrice * 0.05
                        val randomPrice = basePrice + Random.nextDouble(-variance, variance)
                        val roundedPrice = (randomPrice * 100).toLong() / 100.0

                        val dto = StockPriceDto(symbol, roundedPrice)
                        val jsonMessage = json.encodeToString(dto)
                        webSocketClient.send(jsonMessage)
                    }
                }
                delay(PRICE_UPDATE_INTERVAL_MS)
            }
        }
    }

    fun stop() {
        priceGeneratorJob?.cancel()
        priceGeneratorJob = null
        connectionJob?.cancel()
        connectionJob = null
        webSocketClient.disconnect()
    }

    companion object {
        private const val WEBSOCKET_URL = "wss://ws.postman-echo.com/raw"
        private const val PRICE_UPDATE_INTERVAL_MS = 2000L
    }
}
