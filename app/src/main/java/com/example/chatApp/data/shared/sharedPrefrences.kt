package com.example.chatApp.data.shared

import android.content.Context

class sharedPrefrences(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("ChatPreferences", Context.MODE_PRIVATE)

    var username: String?
        get() = sharedPreferences.getString("username", null)
        set(value) = sharedPreferences.edit().putString("username", value).apply()

    var roomName: String?
        get() = sharedPreferences.getString("roomName", null)
        set(value) = sharedPreferences.edit().putString("roomName", value).apply()

}