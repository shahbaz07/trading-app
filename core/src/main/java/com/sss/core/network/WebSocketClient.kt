package com.sss.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WebSocketClient {
    val state: StateFlow<WebSocketState>
    fun connect(url: String): Flow<String>
    fun send(message: String): Boolean
    fun disconnect()
    fun isConnected(): Boolean
}
