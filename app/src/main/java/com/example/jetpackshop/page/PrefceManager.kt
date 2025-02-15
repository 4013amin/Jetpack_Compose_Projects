package com.example.jetpackshop.page

import android.content.Context
import android.content.SharedPreferences

object PrefceManager {

    const val PREFS_NAME = "saved_products_prefs"
    private const val USERNAME_KEY = "username"
    private const val PASSWORD_KEY = "password"
    private const val IS_CHECK_KEY = "is_check"


    private fun getPreferences(con: Context): SharedPreferences {
        return con.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    fun saveUsername(context: Context, username: String) {
        getPreferences(context).edit().putString(USERNAME_KEY, username).apply()
    }

    fun getUsername(context: Context): String {
        return getPreferences(context).getString(USERNAME_KEY, "")!!
    }

    fun savepassword(context: Context, password: String) {
        getPreferences(context).edit().putString(PASSWORD_KEY, password).apply()
    }

    fun getpassword(context: Context): String {
        return getPreferences(context).getString(PASSWORD_KEY, "")!!
    }

    fun saveIcheck(context: Context, ischeck: Boolean) {
        getPreferences(context).edit().putBoolean(IS_CHECK_KEY, ischeck).apply()
    }

    fun getIcheck(context: Context): Boolean {
        return getPreferences(context).getBoolean(IS_CHECK_KEY, false)
    }
}