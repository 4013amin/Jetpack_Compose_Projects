package com.example.jetpackshop.Websocket.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

class MainUiWebsocket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
                WebSocketChatUI()
            }
        }
    }
}

@Composable
fun WebSocketChatUI() {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val scope = rememberCoroutineScope()

    // Create an instance of WebSocketClient
    val webSocketClient = remember { WebSocketClient(scope) }

    DisposableEffect(Unit) {
        // Establish WebSocket connection
        webSocketClient.connectWebSocket("ws://192.168.249.101:2020/ws/app/lobby/") {
            val jsonMessage = JSONObject(it)
            val content = jsonMessage.optString("message", "").trim()
            val isSentByUser = jsonMessage.optBoolean("isSentByUser", false)

            // Only add messages that are not sent by the user
            if (content.isNotEmpty() && !isSentByUser) {  // <-- Only add messages from other users
                messages = messages + ChatMessage(content, isSentByUser)
            } else {
                Log.e(
                    "WebSocketChatUI",
                    "Received empty content in message or message from self"
                )  // <-- Logging for debug
            }
        }

        onDispose {
            webSocketClient.closeWebSocket()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { msg ->
                MessageBubble(message = msg.content, isSentByUser = msg.isSentByUser)
            }
        }
        Divider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                placeholder = { Text("Type a message...") }
            )
            IconButton(
                onClick = {
                    if (message.isNotEmpty()) {
                        val jsonMessage = JSONObject().apply {
                            put("message", message.trim())  // <-- Sending message
                            put("isSentByUser", true)
                        }.toString()
                        webSocketClient.sendMessage(jsonMessage)
                        // Add the message to the list of messages sent by the user
                        // Note: This is to show the message immediately to the user
                        messages = messages + ChatMessage(message.trim(), true)
                        message = ""
                    } else {
                        Log.e(
                            "WebSocketChatUI",
                            "Message field is missing or empty"
                        )  // <-- Logging for debug
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Message"
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: String, isSentByUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = if (isSentByUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = message,
            modifier = Modifier
                .background(if (isSentByUser) Color(0xFFDCF8C6) else Color.White)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

data class ChatMessage(val content: String, val isSentByUser: Boolean)

class WebSocketClient(private val scope: CoroutineScope) {

    private lateinit var webSocket: WebSocket

    fun connectWebSocket(url: String, onMessageReceived: (String) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i("WebSocket", "WebSocket connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("WebSocket", "Message received: $text")
                scope.launch(Dispatchers.Main) {
                    onMessageReceived(text)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Handle binary messages if needed
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("WebSocket", "WebSocket closing: $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("WebSocket", "WebSocket closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "WebSocket failure: ${t.message}", t)
            }
        }

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        Log.i("WebSocketClient", "Sending message: $message")
        webSocket.send(message)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Goodbye!")
    }
}
