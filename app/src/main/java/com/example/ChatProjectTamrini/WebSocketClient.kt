import okhttp3.*
import okio.ByteString
import kotlinx.coroutines.*

class WebSocketClient(private val username: String) {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connectToWebSocket() {
        val request = Request.Builder()
            .url("ws://192.168.10.101:2020/ws/app/username/")
            .build()

        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                val jsonMessage = """{"username": "$username", "message": "Connected"}"""
                webSocket.send(jsonMessage)
            }


            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Received: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("Received bytes: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                println("Closing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Error: ${t.message}")
            }
        }

        webSocket = client.newWebSocket(request, webSocketListener)
    }

    fun sendMessage(message: String) {
        val jsonMessage = """{"username": "$username", "message": "$message"}"""
        webSocket.send(jsonMessage)
    }


    fun closeWebSocket() {
        webSocket.close(1000, "Closing connection")
    }
}
