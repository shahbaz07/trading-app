package com.sss.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OkHttpWebSocketClient @Inject constructor() : WebSocketClient {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    private var webSocket: WebSocket? = null

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected())
    override val state: StateFlow<WebSocketState> = _state.asStateFlow()

    override fun connect(url: String): Flow<String> = callbackFlow {
        _state.value = WebSocketState.Connecting

        val request = Request.Builder()
            .url(url)
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _state.value = WebSocketState.Connected
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _state.value = WebSocketState.Disconnected(reason)
                channel.close()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _state.value = WebSocketState.Error(t)
                channel.close(t)
            }
        }

        webSocket = client.newWebSocket(request, listener)

        awaitClose {
            disconnect()
        }
    }

    override fun send(message: String): Boolean {
        return webSocket?.send(message) ?: false
    }

    override fun disconnect() {
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        _state.value = WebSocketState.Disconnected()
    }

    override fun isConnected(): Boolean {
        return _state.value == WebSocketState.Connected
    }
}
