package com.example.jetpackshop.Websocket.data.Api

import com.example.jetpackshop.Websocket.ui.ChatMessage
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("chats/{roomName}/messages/")
    suspend fun getMessages(@Path("roomName") roomName: String): List<ChatMessage>
}
