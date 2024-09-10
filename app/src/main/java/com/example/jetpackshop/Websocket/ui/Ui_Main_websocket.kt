    package com.example.jetpackshop.Websocket.ui
    
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
            composable(
                "DirectChat/{receiverUsername}/{senderUsername}",
                arguments = listOf(
                    navArgument("receiverUsername") { type = NavType.StringType },
                    navArgument("senderUsername") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val receiverUsername = backStackEntry.arguments?.getString("receiverUsername") ?: ""
                val senderUsername = backStackEntry.arguments?.getString("senderUsername") ?: ""
                DirectChatScreen(receiverUsername, senderUsername, navController)
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
    
    data class User(val username: String, val profileImage: String)
    
    @Composable
    fun WebSocketChatUI(username: String, roomName: String, navController: NavController) {
        val users = remember { mutableStateListOf<User>() }
        val scope = rememberCoroutineScope()
        val webSocketClient = remember { WebSocketClient(scope) }
    
        LaunchedEffect(roomName) {
            val url = "ws://192.168.1.105:2020/ws/app/$roomName/$username/"
            webSocketClient.connectWebSocket(url) { message ->
                try {
                    val jsonObject = JSONObject(message)
                    if (jsonObject.optString("type") == "user_list") {
                        val jsonArray = jsonObject.getJSONArray("users")
                        val fetchedUsers = mutableListOf<User>()
                        for (i in 0 until jsonArray.length()) {
                            val userName = jsonArray.getString(i)
                            fetchedUsers.add(User(username = userName, profileImage = ""))
                        }
                        users.clear()
                        users.addAll(fetchedUsers)
                    }
                } catch (e: JSONException) {
                    println("Failed to parse WebSocket message: ${e.message}")
                }
            }
        }
    
        Column {
            Text(text = "Chat Room: $roomName", style = MaterialTheme.typography.titleMedium)
    
            UsersList(
                users = users,
                onUserClick = { clickedUser ->
                    navController.navigate("DirectChat/${clickedUser.username}/$username")
                }
            )
        }
    }
    
    
    @Composable
    fun DirectChatScreen(
        receiverUsername: String,
        senderUsername: String,
        navController: NavController
    ) {
        var message by remember { mutableStateOf("") }
        val messages = remember { mutableStateListOf<Pair<String, String>>() }
        val scope = rememberCoroutineScope()
        val webSocketClient = remember { WebSocketClient(scope) }
        val context = LocalContext.current
    
        DisposableEffect(Unit) {
            val url = "ws://192.168.1.105:2020/ws/app/$receiverUsername/$senderUsername/"
            webSocketClient.connectWebSocket(url) { receivedMessage ->
                val json = JSONObject(receivedMessage)
                if (json.optString("type") == "direct_message") {
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
                text = "Chat with $receiverUsername",
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
                            put("sender", senderUsername)
                            put("receiver", receiverUsername)
                            put("type", "direct_message")
                        }.toString()
                        webSocketClient.sendMessage(jsonMessage)
                        messages.add(senderUsername to message)
                        message = ""
                    }
                }) {
                    Icon(Icons.Filled.Send, contentDescription = "Send Message")
                }
            }
        }
    }
    
    @Composable
    fun UsersList(
        users: List<User>,
        onUserClick: (User) -> Unit
    ) {
        LazyColumn {
            items(users) { user ->
                UserListItem(user = user, onClick = { onUserClick(user) })
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
    
    
    suspend fun FetchUsersFromServer(roomName: String): List<User> {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url("ws://192.168.1.105:2020/ws/app/$roomName/").build()
            val response = client.newCall(request).execute()
            val jsonResponse = response.body?.string() ?: ""
    
            // Log the response for debugging
            println("Server Response: $jsonResponse")
    
            val jsonObject: JSONObject
            try {
                jsonObject = JSONObject(jsonResponse)
            } catch (e: JSONException) {
                println("Failed to parse JSON: ${e.message}")
                return@withContext emptyList<User>()
            }
    
            // Assuming the server responds with a JSON array of users
            val users = mutableListOf<User>()
            if (jsonObject.optString("type") == "user_list") {
                val jsonArray = jsonObject.getJSONArray("users")
                for (i in 0 until jsonArray.length()) {
                    val userName = jsonArray.getString(i)
                    users.add(User(username = userName, profileImage = ""))
                }
            }
            users
        }
    }
    
    
    @Preview(showBackground = true)
    @Composable
    fun showLoginChat() {
        val navController = rememberNavController()
        ScreenLoginWithProfileImage(navController)
    }
    
