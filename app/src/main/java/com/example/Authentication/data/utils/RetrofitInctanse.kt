package com.example.Authentication.data.utils


import Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInctanse {


    const val url = "http://192.168.1.101:2020/"


    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

}