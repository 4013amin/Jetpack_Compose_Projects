package com.example.jetpackshop.NewProjectForMe.data.utils

import com.example.jetpackshop.NewProjectForMe.data.api.Apis
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrodiInctanse {

    const val bas = "http://192.168.149.101:2020/"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: Apis by lazy {
        Retrofit.Builder()
            .baseUrl(bas)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Apis::class.java)
    }


}