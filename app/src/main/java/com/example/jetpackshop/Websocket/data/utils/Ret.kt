package com.example.jetpackshop.Websocket.data.utils

import com.example.jetpackshop.Websocket.data.Api.apiwebsockt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Ret {

    const val url = ""

    val api by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiwebsockt::class.java)
    }

}