package com.example.jetpackshop.Websocket.data.shared

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ChatPreferences", Context.MODE_PRIVATE)

    var username: String?
        get() = sharedPreferences.getString("username", null)
        set(value) = sharedPreferences.edit().putString("username", value).apply()

    var roomName: String?
        get() = sharedPreferences.getString("roomName", null)
        set(value) = sharedPreferences.edit().putString("roomName", value).apply()
}
