package com.example.jetpackshop.Websocket.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.media.*
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import okhttp3.*
import org.json.JSONObject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
class VoiceChatViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "VoiceChatViewModel"
    private lateinit var webSocket: WebSocket
    private val sampleRate = 44100  // نرخ نمونه‌برداری استاندارد
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    private lateinit var audioRecord: AudioRecord
    private lateinit var audioTrack: AudioTrack
    private var udpSocket: DatagramSocket? = null

    init {
        // Initialize AudioRecord (ضبط) و AudioTrack (پخش)
        audioRecord = AudioRecord.Builder()
            .setAudioSource(MediaRecorder.AudioSource.MIC)
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .build()

        audioTrack = AudioTrack.Builder()
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    // اتصال به WebSocket برای ارسال پیام‌های signaling
    fun connectToWebSocket(username: String, targetUserId: String) {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://192.168.1.110:2020/ws/voice-chat/$username/")  // آدرس WebSocket را به روز کنید
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket Opened")
                startAudioStreaming() // شروع ضبط و ارسال صدا
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleWebSocketMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket Failure", t)
            }
        })
    }

    // مدیریت پیام‌های WebSocket
    private fun handleWebSocketMessage(message: String) {
        val data = JSONObject(message)
        when (data.getString("type")) {
            "offer" -> {
                // پردازش offer دریافت شده
            }

            "answer" -> {
                // پردازش answer دریافت شده
            }

            "candidate" -> {
                // پردازش ICE candidate یا اطلاعات اتصال
            }
        }
    }

    // شروع ضبط و ارسال صدا از طریق UDP
    private fun startAudioStreaming() {
        udpSocket = DatagramSocket()
        audioRecord.startRecording()

        val buffer = ByteArray(bufferSize)
        val address = InetAddress.getByName("192.168.1.110") // IP سرور

        Thread {
            while (true) {
                val read = audioRecord.read(buffer, 0, buffer.size)
                if (read > 0) {
                    val packet =
                        DatagramPacket(buffer, read, address, 12345) // ارسال UDP به پورت 12345
                    udpSocket?.send(packet)
                }
            }
        }.start()
    }

    fun startReceivingAudio() {
        val socket = DatagramSocket(12345)
        val buffer = ByteArray(bufferSize)
        audioTrack.play()

        Thread {
            while (true) {
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                audioTrack.write(packet.data, 0, packet.length)
            }
        }.start()
    }


    // توقف استریم صدا
    fun stopVoiceStreaming() {
        audioRecord.stop()
        audioTrack.stop()
        udpSocket?.close()
    }

    // قطع WebSocket و توقف صدا
    fun disconnectVoiceChat() {
        webSocket.close(1000, "Call ended")
        stopVoiceStreaming()
    }
}
