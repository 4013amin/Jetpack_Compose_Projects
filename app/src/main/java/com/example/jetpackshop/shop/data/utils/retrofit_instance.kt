package com.example.jetpackshop.shop.data.utils

import com.example.jetpackshop.shop.data.Apis.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object retrofit_instance {

    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(urls.baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

}