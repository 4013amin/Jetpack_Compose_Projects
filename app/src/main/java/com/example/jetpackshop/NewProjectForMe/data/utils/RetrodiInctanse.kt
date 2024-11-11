package com.example.jetpackshop.NewProjectForMe.data.utils

import com.example.jetpackshop.NewProjectForMe.data.api.Apis
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrodiInctanse {

    const val baseurl = "http://"

    val api: Apis by lazy {
        Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Apis::class.java)
    }

}