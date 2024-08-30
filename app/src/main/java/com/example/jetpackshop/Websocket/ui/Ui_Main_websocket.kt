package com.example.jetpackshop.Websocket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpackshop.Websocket.data.shared.PreferencesManager
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
                val navController = rememberNavController()
                MainNavigation(navController)
            }
        }
    }
}

@Composable
fun MainNavigation(navController: NavController) {
    NavHost(navController = navController as NavHostController, startDestination = "LoginScreen") {
        composable("LoginScreen") {
            ScreenLogin(navController)
        }
        composable(
            "ChatScreen/{username}/{roomName}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("roomName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            WebSocketChatUI(username, roomName)
        }
    }
}

@Composable
fun ScreenLogin(navController: NavController) {
    val context = navController.context
    val preferencesManager = remember { PreferencesManager(context) }
    var username by remember { mutableStateOf(preferencesManager.username ?: "") }
    var roomName by remember { mutableStateOf(preferencesManager.roomName ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Enter your username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        TextField(
            value = roomName,
            onValueChange = { roomName = it },
            placeholder = { Text("Enter room name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isNotEmpty() && roomName.isNotEmpty()) {
                    val cleanUsername = username.trim()
                    val cleanRoomName = roomName.trim()

                    // Save the data in SharedPreferences
                    preferencesManager.username = cleanUsername
                    preferencesManager.roomName = cleanRoomName

                    navController.navigate("ChatScreen/$cleanUsername/$cleanRoomName")
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Login")
        }
    }
}

@Composable
fun WebSocketChatUI(username: String, roomName: String) {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, String>>() }
    val scope = rememberCoroutineScope()

    val webSocketClient = remember { WebSocketClient(scope) }
    var isConnected by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val url = "wss://mywebsocket.liara.run/ws/app/$roomName/$username/"
//        val url = "ws://192.168.254.101:2020/ws/app/$roomName/$username/"
        webSocketClient.connectWebSocket(url) { receivedMessage ->
            val json = JSONObject(receivedMessage)
            val sender = json.getString("sender")
            val messageText = json.getString("message")
            messages.add(sender to messageText)
        }
        isConnected = true

        onDispose {
            webSocketClient.closeWebSocket()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat Room: $roomName",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Divider(color = Color.Gray, thickness = 1.dp)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages.reversed()) { (sender, msg) ->
                MessageBubble(sender = sender, message = msg)
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
                            put("sender", username)
                        }.toString()

                        webSocketClient.sendMessage(jsonMessage)
                        messages.add(username to message)
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

@Composable
fun MessageBubble(sender: String, message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFFFFFF))
                .padding(8.dp)
                .widthIn(max = 240.dp)
        ) {
            Text(text = sender, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(text = message, color = Color.Black)
        }
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
        if (::webSocket.isInitialized) {
            webSocket.send(message)
        } else {
            println("WebSocket is not initialized or connected.")
        }
    }

    fun closeWebSocket() {
        if (::webSocket.isInitialized) {
            webSocket.close(1000, "Goodbye!")
        }
    }
}

