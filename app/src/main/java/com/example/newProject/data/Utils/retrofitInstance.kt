package com.example.newProject.data.Utils

import com.example.newProject.data.Apis.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retrofitInstance {

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(urls.urls)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}