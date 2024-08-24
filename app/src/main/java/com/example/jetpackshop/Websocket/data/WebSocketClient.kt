import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketClient {

    private lateinit var webSocket: WebSocket

    fun connectWebSocket(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                // اتصال WebSocket باز شده است
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // پیام متنی دریافت شده
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // پیام باینری دریافت شده
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // اتصال WebSocket در حال بسته شدن است
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                // اتصال WebSocket بسته شده است
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                // اتصال WebSocket شکست خورده است
            }
        }

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Goodbye!")
    }
}
