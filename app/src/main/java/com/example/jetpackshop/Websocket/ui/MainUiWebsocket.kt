import android.os.Bundle
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
    val client = OkHttpClient()

    val request = Request.Builder().url("ws://192.168.1.110:2020/ws/app/").build()
    val listener = object : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            GlobalScope.launch(Dispatchers.Main) {
                messages += "$text\n"
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            GlobalScope.launch(Dispatchers.Main) {
                messages += "Error: ${t.message}\n"
            }
        }
    }

    val webSocket = client.newWebSocket(request, listener)

    Column(Modifier.padding(16.dp)) {
        Text(text = messages)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = message, onValueChange = { message = it })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (message.isNotEmpty()) {
                webSocket.send(message)
                message = ""
            }
        }) {
            Text("Send")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WebSocketDemoPreview() {
    WebSocketDemo()
}