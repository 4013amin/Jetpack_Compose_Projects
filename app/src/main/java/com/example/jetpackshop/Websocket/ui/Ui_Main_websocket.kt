package com.example.jetpackshop.Websocket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
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
    var username by remember { mutableStateOf("") }
    var roomName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val scope = rememberCoroutineScope()

    val webSocketClient = remember { WebSocketClient(scope) }
    var isConnected by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            webSocketClient.closeWebSocket()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (!isConnected) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("Enter your username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                TextField(
                    value = roomName,
                    onValueChange = { roomName = it },
                    placeholder = { Text("Enter room name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (username.isNotEmpty() && roomName.isNotEmpty()) {
                        // حذف فاصله‌های اضافی
                        val cleanUsername = username.trim()
                        val cleanRoomName = roomName.trim()

                        val url = "ws://192.168.1.110:2020/ws/app/$cleanRoomName/$cleanUsername/"
                        webSocketClient.connectWebSocket(url) { text ->
                            // بقیه کدها به همان صورت قبل
                        }
                        isConnected = true
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Connect")
            }

        } else {
            // نمایش صفحه چت بدون کاربران آنلاین
            Text(
                text = "Chat Room: $roomName",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(messages.reversed()) { msg ->
                    MessageBubble(message = msg.content, isSentByUser = msg.isSentByUser)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )
                IconButton(
                    onClick = {
                        if (message.isNotEmpty()) {
                            val jsonMessage = JSONObject().apply {
                                put("message", message)
                            }.toString()

                            webSocketClient.sendMessage(jsonMessage)
                            messages.add(ChatMessage(message, true))
                            message = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Message",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
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
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSentByUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF))
                .padding(8.dp)
                .widthIn(max = 240.dp),
            color = Color.Black
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
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                println("WebSocket connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                scope.launch(Dispatchers.Main) {
                    onMessageReceived(text)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Handle binary messages if needed
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket closing: $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket closed: $reason")
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: okhttp3.Response?
            ) {
                println("WebSocket failure: ${t.message}")
            }
        }

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Goodbye!")
    }
}

