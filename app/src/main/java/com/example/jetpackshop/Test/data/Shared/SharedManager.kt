package com.example.jetpackshop.Test.data.Shared

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var username: String?
        get() = sharedPreferences.getString("username", null)
        set(value) = sharedPreferences.edit { putString("username", value) }
    var password: String?
        get() = sharedPreferences.getString("password", null)
        set(value) = sharedPreferences.edit { putString("password", value) }

}