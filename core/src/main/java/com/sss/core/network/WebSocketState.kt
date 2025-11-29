package com.sss.core.network

sealed class WebSocketState {
    data object Connecting : WebSocketState()
    data object Connected : WebSocketState()
    data class Disconnected(val reason: String? = null) : WebSocketState()
    data class Error(val throwable: Throwable) : WebSocketState()
}
