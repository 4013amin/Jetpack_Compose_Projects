package com.example.Authentication.data.shared

import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import com.example.jetpackshop.page.PrefceManager

object SharedPrefers {

    private val PREFS_NAME = "saved_products_prefs"
    private val USERNAME = "username"
    private val PASSWORD = "password"
    private val EMAIL = "email"


    private fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(PrefceManager.PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUsername(context: Context, username: String) {
        getPreference(context).edit().putString(USERNAME, username).apply()
    }

    fun getUsername(context: Context): String {
        return getPreference(context).getString(USERNAME, "")!!
    }

    fun savePassword(context: Context, password: String) {
        getPreference(context).edit().putString(PASSWORD, password).apply()
    }

    fun getPassword(context: Context): String {
        return getPreference(context).getString(PASSWORD, "")!!
    }

    fun saveEmail(context: Context, email: String) {
        getPreference(context).edit().putString(EMAIL, email).apply()
    }

    fun getEmail(context: Context): String {
        return getPreference(context).getString(EMAIL, "")!!
    }


}