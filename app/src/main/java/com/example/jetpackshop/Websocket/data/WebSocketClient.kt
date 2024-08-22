package com.example.jetpackshop.Websocket.data

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketClient(url: String) {
    private val client = OkHttpClient()
    private val request = Request.Builder().url(url).build()
    private val webSocket: WebSocket = client.newWebSocket(request, WebSocketListenerImpl())

    var onMessage: (String) -> Unit = {}
    var onError: (String) -> Unit = {}
    var onOpen: () -> Unit = {}
    var onClose: () -> Unit = {}

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    private inner class WebSocketListenerImpl : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            super.onOpen(webSocket, response)
            onOpen()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            onMessage(text)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            super.onFailure(webSocket, t, response)
            onError(t.message ?: "Unknown error")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            webSocket.close(code, reason)
            onClose()
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            onClose()
        }
    }
}
