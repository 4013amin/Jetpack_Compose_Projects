package com.example.ChatProjectTamrini

import WebSocketClient
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class ChatMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatScreen()
        }
    }
}


@Composable
fun ChatScreen() {
    val username = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val messagesList = remember { mutableStateListOf<String>() }
    val webSocketClient = remember { WebSocketClient(username.value) }

    Column {
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") }
        )

        Button(onClick = {
            webSocketClient.connectToWebSocket()
        }) {
            androidx.compose.material3.Text(text = "connect")
        }

        TextField(
            value = message.value,
            onValueChange = { message.value = it },
            label = { Text("Message") }
        )

        Button(onClick = {
            // Send message to WebSocket
            webSocketClient.sendMessage(message.value)
        }) {
            androidx.compose.material3.Text(text = "send")
        }

        LazyColumn {
            items(messagesList) { msg ->
                androidx.compose.material3.Text(text = msg)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun showChatScreen() {
    ChatScreen()
}