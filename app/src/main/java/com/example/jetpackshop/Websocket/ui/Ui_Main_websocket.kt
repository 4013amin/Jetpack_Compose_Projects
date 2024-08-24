package com.example.jetpackshop.Websocket.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class MainUiWebsocket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
                WebSocketDemo()
            }
        }
    }
}

@Composable
fun WebSocketDemo() {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf("") }
    var sentMessage by remember { mutableStateOf("") }
    val client = OkHttpClient()
    val request = Request.Builder().url("ws://192.168.1.110:2020/ws/app/").build()

    val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i("WebSocketStatus", "WebSocket connected successfully")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            GlobalScope.launch(Dispatchers.Main) {
                messages += "$text\n"
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            GlobalScope.launch(Dispatchers.Main) {
                messages += "Error: ${t.message}\n"
                Log.e("WebSocketError", "Error: ${t.message}", t)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            GlobalScope.launch(Dispatchers.Main) {
                messages += "WebSocket closed: $reason\n"
            }
        }
    }

    val webSocket = client.newWebSocket(request, listener)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = messages)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = message,
            onValueChange = { message = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (message.isNotEmpty()) {
                val jsonMessage = JSONObject().put("message", message).toString()
                val sendSuccess = webSocket.send(jsonMessage)
                if (sendSuccess) {
                    // پیام با موفقیت ارسال شد
                    GlobalScope.launch(Dispatchers.Main) {
                        sentMessage = "Message sent successfully: $message"
                        message = ""
                    }
                } else {
                    // ارسال پیام با خطا مواجه شد
                    GlobalScope.launch(Dispatchers.Main) {
                        sentMessage = "Failed to send message: $message"
                    }
                }
            }
        }) {
            Text("Send")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = sentMessage)
    }
}
