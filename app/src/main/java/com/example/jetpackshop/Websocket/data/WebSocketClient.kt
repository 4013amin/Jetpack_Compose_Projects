//import android.util.Log
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.WebSocket
//import okhttp3.WebSocketListener
//import org.json.JSONObject
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import okhttp3.Response
//import okio.ByteString
//
//class WebSocketClient(private val scope: CoroutineScope) {
//
//    private lateinit var webSocket: WebSocket
//
//    fun connectWebSocket(url: String, onMessageReceived: (String) -> Unit) {
//        val client = OkHttpClient()
//        val request = Request.Builder().url(url).build()
//        val listener = object : WebSocketListener() {
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                Log.i("WebSocket", "WebSocket connected")
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                Log.i("WebSocket", "Message received: $text")
//                scope.launch(Dispatchers.Main) {
//                    onMessageReceived(text)
//                }
//            }
//
//            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                // Handle binary messages if needed
//            }
//
//            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                Log.i("WebSocket", "WebSocket closing: $reason")
//            }
//
//            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//                Log.i("WebSocket", "WebSocket closed: $reason")
//            }
//
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                Log.e("WebSocket", "WebSocket failure: ${t.message}", t)
//            }
//        }
//
//        webSocket = client.newWebSocket(request, listener)
//    }
//
//    fun sendMessage(message: String) {
//        webSocket.send(message)
//    }
//
//    fun closeWebSocket() {
//        webSocket.close(1000, "Goodbye!")
//    }
//}