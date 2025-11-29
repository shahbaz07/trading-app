package com.sss.feature.stock.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.core.network.WebSocketState
import com.sss.feature.stock.domain.usecase.GetConnectionStateUseCase
import com.sss.feature.stock.domain.usecase.GetStockPricesUseCase
import com.sss.feature.stock.domain.usecase.StartPriceFeedUseCase
import com.sss.feature.stock.domain.usecase.StopPriceFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getStockPricesUseCase: GetStockPricesUseCase,
    private val startPriceFeedUseCase: StartPriceFeedUseCase,
    private val stopPriceFeedUseCase: StopPriceFeedUseCase,
    private val getConnectionStateUseCase: GetConnectionStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StockUiState())
    val uiState: StateFlow<StockUiState> = _uiState.asStateFlow()

    private var stockCollectionJob: Job? = null

    init {
        observeConnectionState()
    }

    fun processIntent(intent: StockIntent) {
        when (intent) {
            is StockIntent.StartFeed -> startFeed()
            is StockIntent.StopFeed -> stopFeed()
            is StockIntent.ToggleFeed -> toggleFeed()
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            getConnectionStateUseCase().collect { state ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isConnected = state == WebSocketState.Connected,
                        isLoading = state == WebSocketState.Connecting,
                        error = when (state) {
                            is WebSocketState.Error -> state.throwable.message
                            is WebSocketState.Disconnected -> if (currentState.isFeedActive) state.reason else null
                            else -> null
                        }
                    )
                }
            }
        }
    }

    private fun startFeed() {
        if (_uiState.value.isFeedActive) return

        _uiState.update { it.copy(isFeedActive = true, isLoading = true, error = null) }
        startPriceFeedUseCase(viewModelScope)

        stockCollectionJob = viewModelScope.launch {
            getStockPricesUseCase()
                .catch { throwable ->
                    _uiState.update { it.copy(error = throwable.message, isLoading = false) }
                }
                .collect { stocks ->
                    _uiState.update { it.copy(stocks = stocks, isLoading = false) }
                }
        }
    }

    private fun stopFeed() {
        stockCollectionJob?.cancel()
        stockCollectionJob = null
        stopPriceFeedUseCase()
        _uiState.update { it.copy(isFeedActive = false, isConnected = false, isLoading = false) }
    }

    private fun toggleFeed() {
        if (_uiState.value.isFeedActive) {
            stopFeed()
        } else {
            startFeed()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopFeed()
    }
}
