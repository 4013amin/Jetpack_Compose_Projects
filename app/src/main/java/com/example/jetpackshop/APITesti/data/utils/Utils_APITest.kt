package com.example.jetpackshop.APITesti.data.utils

import com.example.jetpackshop.APITesti.data.apis.APITesti
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val baseNewUrl = "http://192.168.1.105:8000/"


object Utils_APITest {


    val API by lazy {
        Retrofit.Builder()
            .baseUrl(baseNewUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APITesti::class.java)
    }
}