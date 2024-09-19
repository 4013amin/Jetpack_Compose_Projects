package com.example.jetpackshop.Websocket.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.jetpackshop.Websocket.ViewModel.VoiceChatViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshop.ui.theme.JetPackShopTheme


class Screens : ComponentActivity() {
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for microphone permission before setting the content
        checkMicrophonePermission()

        setContent {
            JetPackShopTheme {
                // Display the ConnectedVoiceChat composable
                ConnectedVoiceChat()
            }
        }
    }

    // Function to check microphone permission
    private fun checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        }
    }

    // Handle the result of permission request
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with voice recording
            } else {
                // Permission denied, handle it appropriately (e.g., show a message)
            }
        }
    }
}

@Composable
fun ConnectedVoiceChat(viewModel: VoiceChatViewModel = viewModel()) {
    var userId by remember { mutableStateOf("") }
    var targetUserId by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Input field for username
        BasicTextField(
            value = userId,
            onValueChange = { userId = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp),
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
        )

        // Input field for target user ID
        BasicTextField(
            value = targetUserId,
            onValueChange = { targetUserId = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp),
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to start the voice chat
        Button(
            onClick = {
                if (userId.isNotEmpty() && targetUserId.isNotEmpty()) {
                    viewModel.connectToWebSocket(
                        userId,
                        targetUserId
                    )  // Connect with both userId and targetUserId
                    viewModel.startReceivingAudio()
                    isConnected = true
                }
            },
            enabled = !isConnected
        ) {
            Text(text = "Start Voice Call")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to disconnect from the voice chat
        Button(
            onClick = {
                if (isConnected) {
                    viewModel.disconnectVoiceChat()
                    isConnected = false
                }
            },
            enabled = isConnected
        ) {
            Text(text = "End Call")
        }
    }
}
