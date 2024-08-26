package com.example.jetpackshop.Websocket.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
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
    var username by remember { mutableStateOf("") }
    var roomName by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val onlineUsers = remember { mutableStateListOf<String>() }
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
                        val url = "ws://192.168.254.101:2020/ws/app/$roomName/$username/"
                        webSocketClient.connectWebSocket(url) { text ->
                            val jsonMessage = JSONObject(text)

                            if (jsonMessage.has("online_users")) {
                                val usersArray = jsonMessage.getJSONArray("online_users")
                                onlineUsers.clear()
                                for (i in 0 until usersArray.length()) {
                                    val user = usersArray.getString(i)
                                    onlineUsers.add(user)
                                }
                            } else {
                                val content = jsonMessage.optString("message", "").trim()
                                val sender = jsonMessage.optString("sender", "").trim()

                                if (content.isNotEmpty()) {
                                    messages.add(ChatMessage(content, sender == username))
                                }
                            }
                        }
                        isConnected = true
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Connect")
            }
        } else {
            if (recipient.isEmpty()) {
                Text(
                    text = "Online Users:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Divider(color = Color.Gray, thickness = 1.dp)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(onlineUsers) { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE0E0E0))
                                .padding(8.dp)
                                .clickable {
                                    recipient = user
                                    messages.clear()
                                }
                        ) {
                            Text(
                                text = user,
                                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Chat with $recipient",
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

@Preview
@Composable
private fun showUiWebSocket() {
    WebSocketChatUI()
}
