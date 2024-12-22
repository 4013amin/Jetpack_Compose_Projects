package com.example.jetpackshop.Websocket.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshop.Websocket.ViewModel.VoiceChatViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class MainUiWebsocket : FragmentActivity() {
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
            Authentication()
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
fun Authentication() {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val navController = rememberNavController()

    var isAuthenticated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        activity?.let {
            val biometricPrompt = BiometricPrompt(
                it,
                ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        isAuthenticated = true
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        isAuthenticated = false
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("احراز هویت بیومتریک")
                .setSubtitle("برای وارد شدن از اثر انگشت یا چهره استفاده کنید")
                .setNegativeButtonText("لغو")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }
    if (isAuthenticated) {
        ScreenLoginWithProfileImage(navController)
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


fun compressAndConvertToBase64(bitmap: Bitmap, quality: Int = 50): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}


@Composable
fun ImageUploadButton(username: String, onImageUpload: (String) -> Unit) {
    val context = LocalContext.current
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // پیکربندی لانچر برای دریافت تصویر از گالری
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                selectedImageBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
            }
        }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Select Image")
        }

        selectedImageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.size(100.dp)
            )

            Button(onClick = {
                // فشرده‌سازی و تبدیل به Base64
                val base64Image = compressAndConvertToBase64(bitmap, quality = 50)
                onImageUpload(base64Image)
            }) {
                Text("Upload Image")
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WebSocketChatUI(username: String, roomName: String, navController: NavController) {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, String>>() }
    val scope = rememberCoroutineScope()
    val webSocketClient = remember { WebSocketClient(scope) }
    val viewModel: VoiceChatViewModel = viewModel()

    DisposableEffect(Unit) {
        val url = "ws://192.168.218.101:2020/ws/app/$roomName/$username/"
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
                            isSentByUser = sender == username,
                            onDeleteMessage = {
                            })
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
                        imeAction = ImeAction.Default, // اینجا به جای Send از Default استفاده می‌کنیم
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
                    singleLine = true,

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
                        // اینجا فوکوس را مجدداً تنظیم می‌کنیم تا کیبورد در حالت فارسی بماند
                        focusManager.clearFocus()
                        focusManager.moveFocus(FocusDirection.Enter) // یا دوباره به TextField فوکوس می‌دهیم
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

@Composable
fun ImageMessageBubble(sender: String, imageData: String, isSentByUser: Boolean) {
    val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(if (isSentByUser) Color.Blue else Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp) // Adjust size as needed
                .clickable { /* Handle image click, e.g., open in fullscreen */ }
        )
    }
}


@Composable
fun MessageBubble(
    sender: String,
    message: String,
    isSentByUser: Boolean,
    onDeleteMessage: () -> Unit
) {
    val backgroundColor = if (isSentByUser) Color(0xFF2196F3) else Color(0xFFE0E0E0)
    val textColor = if (isSentByUser) Color.White else Color.Black
    val shape = if (isSentByUser) RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    else RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)


    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Card(
            shape = shape,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            modifier = Modifier
                .align(if (isSentByUser) Alignment.CenterEnd else Alignment.CenterStart)
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { showMenu = true }
                    )
                }
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isSentByUser) {
                    Text(
                        text = sender,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontSize = 12.sp
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

        // DropdownMenu for options
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("حذف پیام") },
                onClick = {
                    onDeleteMessage()
                    showMenu = false
                }
            )
        }
    }
}


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

