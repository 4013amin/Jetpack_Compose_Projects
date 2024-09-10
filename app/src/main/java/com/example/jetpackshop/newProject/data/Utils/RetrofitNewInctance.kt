package com.example.jetpackshop.newProject.data.Utils

import com.example.jetpackshop.newProject.data.Api.ApiNew
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


const val baseNewUrl = "http://192.168.1.105:8000/"

object RetrofitNewInctance {


    val api by lazy {
        Retrofit.Builder()
            .baseUrl(baseNewUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiNew::class.java)

    }


}