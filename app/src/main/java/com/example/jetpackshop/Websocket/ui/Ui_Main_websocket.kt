package com.example.jetpackshop.Websocket.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackshop.R
import com.example.jetpackshop.Websocket.data.shared.PreferencesManager
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException


class MainUiWebsocket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Request necessary permissions
        val requestPermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                // Handle permission result
            }

        requestPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET
            )
        )


        setContent {
            JetPackShopTheme {
                val navController = rememberNavController()
                MainNavigation(navController)
                MainNavigation(navController)
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
fun VoiceRecordingButton(onVoiceRecorded: (File) -> Unit) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var mediaRecorder: MediaRecorder? = remember { null }
    var audioFile: File? by remember { mutableStateOf(null) }
    var showToast by remember { mutableStateOf("") }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showToast = "Permission to record audio is denied."
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    if (showToast.isNotEmpty()) {
        Toast.makeText(context, showToast, Toast.LENGTH_SHORT).show()
        showToast = ""
    }

    Button(
        onClick = {
            if (isRecording) {
                mediaRecorder?.stop()
                mediaRecorder?.release()
                mediaRecorder = null
                isRecording = false
                audioFile?.let(onVoiceRecorded)
            } else {
                try {
                    val fileName =
                        "${context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)}/recording.3gp"
                    audioFile = File(fileName)
                    mediaRecorder = MediaRecorder().apply {
                        setAudioSource(MediaRecorder.AudioSource.MIC)
                        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                        setOutputFile(fileName)
                        prepare()
                        start()
                    }
                    isRecording = true
                } catch (e: IOException) {
                    Toast.makeText(context, "Recording failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isRecording) Color.Red else MaterialTheme.colorScheme.primary
        )
    ) {
        Text(if (isRecording) "Stop Recording" else "Start Recording")
    }
}


@Composable
fun WebSocketChatUI(username: String, roomName: String, navController: NavController) {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, String>>() }
    val scope = rememberCoroutineScope()
    val webSocketClient = remember { WebSocketClient(scope) }

    DisposableEffect(Unit) {
        val url = "ws://192.168.75.101:2020/ws/app/$roomName/$username/"
        webSocketClient.connectWebSocket(url) { receivedMessage ->
            try {
                val json = JSONObject(receivedMessage)
                val sender = json.optString("sender", "Unknown")

                if (json.has("voice_data")) {
                    val voiceData = json.optString("voice_data", "")
                    messages.add(sender to voiceData)
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
                        VoiceMessageBubble(
                            sender = sender,
                            audioData = msg,
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
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("پیام خود را وارد کنید...") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send,
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
                    }
                }) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Send Message",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            VoiceRecordingButton { recordedFile ->
                val audioBytes = recordedFile.readBytes()
                val base64Audio = Base64.encodeToString(audioBytes, Base64.DEFAULT)
                val jsonMessage = JSONObject().apply {
                    put("voice_data", base64Audio)
                    put("sender", username)
                }

                webSocketClient.sendMessage(jsonMessage.toString())
            }
        }
    }
}

@Composable
fun VoiceMessageBubble(sender: String, audioData: String, isSentByUser: Boolean) {
    val backgroundColor = if (isSentByUser) Color(0xFF2196F3) else Color(0xFFE0E0E0)
    val textColor = if (isSentByUser) Color.White else Color.Black
    val alignment = if (isSentByUser) Alignment.End else Alignment.Start
    val shape = if (isSentByUser) RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    else RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    val playAudio = {
        try {
            if (mediaPlayer == null) {
                val decodedBytes = Base64.decode(audioData, Base64.DEFAULT)
                val tempFile = File.createTempFile("tempAudio", "3gp", context.cacheDir).apply {
                    writeBytes(decodedBytes)
                }
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(tempFile.absolutePath)
                    prepare()
                    start()
                    isPlaying = true
                    setOnCompletionListener {
                        it.reset()
                        isPlaying = false
                        mediaPlayer = null
                    }
                }
            } else {
                mediaPlayer?.start()
                isPlaying = true
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to play audio", Toast.LENGTH_SHORT).show()
            Log.e("VoiceMessageBubble", "Error playing audio: ${e.message}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(backgroundColor, shape)
            .padding(8.dp)
            .clickable { playAudio() }
    ) {
        Text(
            text = if (isPlaying) "Playing audio..." else "$sender sent a voice message",
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}




private fun playAudioFromByteArray(audioBytes: ByteArray) {
    val tempFile = File.createTempFile("audio", ".3gp", Environment.getExternalStorageDirectory())
    try {
        tempFile.writeBytes(audioBytes)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(tempFile.absolutePath)
        mediaPlayer.prepare()
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            it.release() // Release the MediaPlayer once playback is complete
            tempFile.delete() // Delete the temporary file after use
        }
    } catch (e: IOException) {
        Log.e("AudioPlayback", "Error playing audio: ${e.message}")
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
    val backgroundColor = if (isSentByUser) Color(0xFF2196F3) else Color(0xFFE0E0E0)
    val textColor = if (isSentByUser) Color.White else Color.Black
    val alignment = if (isSentByUser) Alignment.End else Alignment.Start
    val shape = if (isSentByUser) RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    else RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Card(
            shape = shape,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isSentByUser) {
                    Text(
                        text = sender,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    color = textColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
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
                    println("Received message: $text")
                    onMessageReceived(text)
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

