package com.example.jetpackshop.Websocket.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.google.android.gms.common.api.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import showNotification

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
    }
}


//lotiFiles
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


//@Composable
//fun ProfileImagePicker(onImagePicked: (String) -> Unit) {
//    val context = LocalContext.current
//    val imageUri = remember { mutableStateOf<String?>(null) }
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        imageUri.value = uri?.toString()
//        uri?.let { onImagePicked(it.toString()) }
//    }
//
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        imageUri.value?.let { uri ->
//            Image(
//                painter = rememberAsyncImagePainter(uri),
//                contentDescription = "Profile Image",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .background(Color.Gray)
//            )
//        } ?: run {
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .background(Color.Gray)
//                    .clickable { launcher.launch("image/*") },
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Pick Image", color = Color.White)
//            }
//        }
//    }
//}


@Composable
fun ScreenLoginWithProfileImage(navController: NavController) {
    val context = navController.context
    val preferencesManager = remember { PreferencesManager(context) }
    var username by remember { mutableStateOf(preferencesManager.username ?: "") }
    var roomName by remember { mutableStateOf(preferencesManager.roomName ?: "") }
    var profileImage by remember { mutableStateOf("") }

//    // Profile Image Picker
//    ProfileImagePicker(onImagePicked = { selectedImageUri ->
//        profileImage = selectedImageUri
//    })

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

                    // Create a new user object with username and profile image
                    val newUser = User(username = cleanUsername, profileImage = profileImage)

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


data class User(val username: String, val profileImage: String)

@Composable
fun WebSocketChatUI(username: String, roomName: String, navController: NavController) {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, String>>() }
    val users = remember { mutableStateListOf<User>() }
    val scope = rememberCoroutineScope()
    val webSocketClient = remember { WebSocketClient(scope) }
    val context = LocalContext.current
    var selectedUser by remember { mutableStateOf<User?>(null) }

    DisposableEffect(Unit) {
        val url = "ws://192.168.254.101:2020/ws/app/$roomName/$username/"
        webSocketClient.connectWebSocket(url) { receivedMessage ->
            val json = JSONObject(receivedMessage)
            when (json.optString("type", "")) {
                "chat_message" -> {
                    val sender = json.optString("sender", "Unknown")
                    val messageText = json.optString("message", "No message content")
                    messages.add(sender to messageText)
                    showNotification(context, "New message from $sender", messageText)
                }

                "direct_message" -> {
                    val sender = json.optString("sender", "Unknown")
                    val messageText = json.optString("message", "No message content")
                    messages.add(sender to messageText)
                    showNotification(context, "New direct message from $sender", messageText)
                }

                "user_list" -> {
                    val usersArray = json.optJSONArray("users") ?: return@connectWebSocket
                    users.clear()
                    for (i in 0 until usersArray.length()) {
                        val userObject = usersArray.getJSONObject(i)
                        val user = User(
                            username = userObject.optString("username", "Unknown"),
                            profileImage = userObject.optString("profile_image", "")
                        )
                        users.add(user)
                    }
                }

                else -> {
                    // Handle other message types if needed
                }
            }
        }

        onDispose {
            webSocketClient.closeWebSocket()
        }
    }

    // UI layout
    Row(modifier = Modifier.fillMaxSize()) {
        // Users List Section
        UsersList(
            users = users,
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .background(Color(0xFFF0F0F0))
                .padding(8.dp),
            onUserClick = { clickedUser ->
                selectedUser = clickedUser
            }
        )

        // Chat Section
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                text = "Chat Room: $roomName",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Divider(color = Color.Gray, thickness = 1.dp)

            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
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
                        .padding(end = 8.dp)
                )
                IconButton(onClick = {
                    if (message.isNotEmpty() && selectedUser != null) {
                        val jsonMessage = JSONObject().apply {
                            put("message", message)
                            put("sender", username)
                            put("receiver", selectedUser!!.username)
                            put("type", "direct_message")
                        }.toString()
                        webSocketClient.sendMessage(jsonMessage)
                        messages.add(username to message)
                        message = ""
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send Message")
                }
            }
        }
    }
}


@Composable
fun DirectChatScreen(receiver: User, username: String, navController: NavController) {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, String>>() }
    val scope = rememberCoroutineScope()
    val webSocketClient = remember { WebSocketClient(scope) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val url = "ws://192.168.254.101:2020/ws/app/${receiver.username}/$username/"
        webSocketClient.connectWebSocket(url) { receivedMessage ->
            val json = JSONObject(receivedMessage)
            if (json.optString("type", "") == "direct_message") {
                val sender = json.optString("sender", "Unknown")
                val messageText = json.optString("message", "No message content")
                messages.add(sender to messageText)
                showNotification(context, "New message from $sender", messageText)
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
        Text(
            text = "Chat with ${receiver.username}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Divider(color = Color.Gray, thickness = 1.dp)

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
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
                    .padding(end = 8.dp)
            )
            IconButton(onClick = {
                if (message.isNotEmpty()) {
                    val jsonMessage = JSONObject().apply {
                        put("message", message)
                        put("sender", username)
                        put("receiver", receiver.username)
                        put("type", "direct_message")
                    }.toString()
                    webSocketClient.sendMessage(jsonMessage)
                    messages.add(username to message)
                    message = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send Message")
            }
        }
    }
}

@Composable
fun UsersList(
    users: List<User>,
    modifier: Modifier = Modifier,
    onUserClick: (User) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Users",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                contentDescription = if (expanded) "Collapse" else "Expand"
            )
        }

        if (expanded) {
            LazyColumn {
                items(users) { user ->
                    UserListItem(user = user, onClick = {
                        onUserClick(user)
                    })
                }
            }
        }
    }
}


@Composable
fun UserListItem(user: User, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.profileImage),
            contentDescription = "${user.username}'s profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun MessageBubble(sender: String, message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = if (sender == "You") Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (sender == "You") Color(0xFFE1FFC7) else Color(0xFFF0F0F0))
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

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun connectWebSocket(url: String, onMessageReceived: (String) -> Unit) {
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

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Handle binary messages if needed
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                println("WebSocket closing: $code / $reason")
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
        webSocket?.send(message) ?: println("WebSocket is not connected")
    }

    fun closeWebSocket() {
        webSocket?.close(1000, "Goodbye!") ?: println("WebSocket is not connected")
    }
}

@Preview(showBackground = true)
@Composable
fun showLogin_chat() {
    val navController = rememberNavController()
    ScreenLoginWithProfileImage(navController)
}

