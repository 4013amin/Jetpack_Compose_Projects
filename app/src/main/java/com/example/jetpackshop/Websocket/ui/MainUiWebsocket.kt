import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.jetpackshop.Websocket.data.WebSocketClient

class MainUiWebsocket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
                WebSocketScreen()
            }
        }
    }
}

@Composable
fun WebSocketScreen() {
    var messages = remember { mutableStateListOf<String>() }
    var messageInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val webSocketClient = remember { WebSocketClient("ws://192.168.1.X:8000/ws/app/") }

    // Handle WebSocket messages
    LaunchedEffect(Unit) {
        webSocketClient.onMessage = { message ->
            messages.add("Server: $message")
            println("Received message from server: $message") // لاگ دریافت پیام از سرور
        }
        webSocketClient.onError = { error ->
            messages.add("Error: $error")
            println("WebSocket Error: $error") // لاگ خطای وب‌سوکت
        }
        webSocketClient.onOpen = {
            messages.add("WebSocket is connected.")
            println("WebSocket connected") // لاگ اتصال وب‌سوکت
        }
        webSocketClient.onClose = {
            messages.add("WebSocket is closed.")
            println("WebSocket closed") // لاگ بستن وب‌سوکت
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = "WebSocket Messages:")
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            messages.forEach { message ->
                Text(text = message)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = messageInput,
            onValueChange = { messageInput = it },
            label = { Text("Enter Message") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (messageInput.isNotBlank()) {
                    // اضافه کردن پیام به لیست پیام‌ها برای نمایش به‌صورت آینه‌ای
                    messages.add("You: $messageInput")
                    webSocketClient.sendMessage(messageInput)
                    println("Message Sent: $messageInput") // لاگ ارسال پیام
                    messageInput = "" // Clear the input field
                } else {
                    messages.add("Error: Message cannot be empty")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Message")
        }
    }
}
