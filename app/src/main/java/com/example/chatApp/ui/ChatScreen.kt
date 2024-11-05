package com.example.chatApp.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpackshop.Websocket.ui.ImageMessageBubble
import com.example.jetpackshop.Websocket.ui.ImageUploadButton
import com.example.jetpackshop.Websocket.ui.MessageBubble
import com.example.jetpackshop.Websocket.ui.WebSocketClient
import org.json.JSONException
import org.json.JSONObject


@Composable
fun UserListScreen(users: List<String>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select a user to chat with",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(users) { user ->
                UserItem(username = user) {

                    navController.navigate("chat/$user")
                }
            }
        }
    }
}

@Composable
fun UserItem(username: String, onUserClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onUserClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = username,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(username: String, roomName: String, navController: NavController) {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, String>>() }
    val scope = rememberCoroutineScope()
    val webSocketClient = remember { WebSocketClient(scope) }

    DisposableEffect(Unit) {
        val url = "ws://192.168.136.101:2020/ws/app/$roomName/$username/"
        webSocketClient.connectWebSocket(url) { receivedMessage ->
            try {
                val json = JSONObject(receivedMessage)
                val sender = json.optString("sender", "Unknown")

                if (json.has("image_data")) {
                    val imageData = json.optString("image_data", "Unknown")
                    messages.add(sender to imageData)
                } else {
                    val messageText = json.optString("message", "No message content")
                    messages.add(sender to messageText)
                }
            } catch (e: JSONException) {
                Log.e("WebSocketChatUI", "Failed to parse WebSocket message: ${e.message}")
            }
        }
        onDispose {
            webSocketClient.closeWebSocket()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { (sender, msg) ->
                    if (msg.startsWith("/9j/")) {
                        ImageMessageBubble(
                            sender = sender,
                            imageData = msg,
                            isSentByUser = sender == username
                        )
                    } else {
                        MessageBubble(
                            sender = sender,
                            message = msg,
                            isSentByUser = sender == username
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                val focusManager = LocalFocusManager.current

                TextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("پیام خود را وارد کنید...") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default,
                        capitalization = KeyboardCapitalization.None,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Right
                    ),
                    singleLine = true
                )

                IconButton(onClick = {
                    if (message.isNotEmpty()) {
                        val jsonMessage = JSONObject().apply {
                            put("message", message)
                            put("sender", username)
                        }
                        webSocketClient.sendMessage(jsonMessage.toString())
                        messages.add(username to message)
                        message = ""
                        focusManager.clearFocus()
                        focusManager.moveFocus(FocusDirection.Enter)
                    }
                }) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Send Message",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            ImageUploadButton(username = username) { base64Image ->
                val jsonMessage = JSONObject().apply {
                    put("image_data", base64Image)
                    put("sender", username)
                }
                webSocketClient.sendMessage(jsonMessage.toString())
            }
        }
    }
}