package com.example.jetpackshop.Websocket.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackshop.R
import com.example.jetpackshop.Websocket.data.shared.PreferencesManager
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject
import showNotification
import java.io.InputStream
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.text.input.KeyboardCapitalization


class MainUiWebsocket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
//                val navController = rememberNavController()
//                MainNavigation(navController)
                ConnectedVoiceChat()
            }
        }
    }
}

@Composable
fun MainNavigation(navController: NavController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "LoginScreen"
    ) {
        composable("LoginScreen") {
            ScreenLoginWithProfileImage(navController)
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
            WebSocketChatUI(username, roomName, navController)
        }
        composable(
            "DirectChat/{receiverUsername}/{senderUsername}",
            arguments = listOf(
                navArgument("receiverUsername") { type = NavType.StringType },
                navArgument("senderUsername") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val receiverUsername = backStackEntry.arguments?.getString("receiverUsername") ?: ""
            val senderUsername = backStackEntry.arguments?.getString("senderUsername") ?: ""
        }
    }
}

@Composable
fun LottieAnimationScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loginanimation))
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

@Composable
fun ScreenLoginWithProfileImage(navController: NavController) {
    val context = navController.context
    val preferencesManager = remember { PreferencesManager(context) }
    var username by remember { mutableStateOf(preferencesManager.username ?: "") }
    var roomName by remember { mutableStateOf(preferencesManager.roomName ?: "") }


    Box(modifier = Modifier.fillMaxWidth()) {
        LottieAnimationScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
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

                    // Navigate to ChatScreen
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
fun WebSocketChatUI(username: String, roomName: String, navController: NavController) {
    var message by remember { mutableStateOf("") }
    val messages =
        remember { mutableStateListOf<Pair<String, String>>() } // Store messages as (sender, message)
    val scope = rememberCoroutineScope()
    val webSocketClient = remember { WebSocketClient(scope) }

    // Establish WebSocket connection when this composable is first displayed
    DisposableEffect(Unit) {
        val url = "ws://192.168.1.110:2020/ws/app/$roomName/$username/"
        webSocketClient.connectWebSocket(url) { receivedMessage ->
            try {
                val json = JSONObject(receivedMessage)
                val sender = json.optString("sender", "Unknown")
                val messageText = json.optString("message", "No message content")
                messages.add(sender to messageText) // Add received message to the list
            } catch (e: JSONException) {
                println("Failed to parse WebSocket message: ${e.message}")
            }
        }

        onDispose {
            webSocketClient.closeWebSocket()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display chat messages
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { (sender, msg) ->
                MessageBubble(sender = sender, message = msg, isSentByUser = sender == username)
            }
        }

        // Input field and send button
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text("Enter your message...") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send,
                    capitalization = KeyboardCapitalization.None
                ),
                modifier = Modifier.weight(1f)
            )


            IconButton(onClick = {
                if (message.isNotEmpty()) {
                    val jsonMessage = JSONObject().apply {
                        put("message", message)
                        put("sender", username) // Ensure sender is included correctly
                    }

                    webSocketClient.sendMessage(jsonMessage.toString()) // Send message to the WebSocket
                    messages.add(username to message) // Add sent message to the local list
                    message = "" // Clear input field
                }
            }) {
                Icon(Icons.Filled.Send, contentDescription = "Send Message")
            }
        }
    }
}


fun handleFileSelection(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes()
        inputStream?.close()  // Close the stream to avoid memory leaks
        if (fileBytes != null && fileBytes.isNotEmpty()) {
            Base64.encodeToString(fileBytes, Base64.DEFAULT)
        } else {
            println("File is empty or could not be read")
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Composable
fun MessageBubble(sender: String, message: String, isSentByUser: Boolean) {
    val backgroundColor =
        if (isSentByUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val alignment = if (isSentByUser) Alignment.End else Alignment.Start

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 250.dp)
                .background(backgroundColor)
                .clip(RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                if (!isSentByUser) {
                    Text(text = sender, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = message,
                    color = if (isSentByUser) Color.White else Color.Black
                )
            }
        }
    }
}


data class ChatMessage(val content: String, val isSentByUser: Boolean)

class WebSocketClient(private val scope: CoroutineScope) {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun connectWebSocket(url: String, onMessageReceived: (String) -> Unit) {
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                println("WebSocket connected to: $url")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                scope.launch(Dispatchers.Main) {
                    println("Received message: $text")  // Log received message
                    onMessageReceived(text) // Notify the composable of the received message
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                println("WebSocket is closing: $code / $reason")
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
        if (webSocket != null) {
            println("Sending message: $message")
            webSocket?.send(message)
        } else {
            println("WebSocket is not connected")
        }
    }

    fun closeWebSocket() {
        webSocket?.close(1000, "Goodbye!") ?: println("WebSocket is not connected")
    }
}

