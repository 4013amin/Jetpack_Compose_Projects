package com.example.jetpackshop.Tamrini.data.utils

import com.example.jetpackshop.Tamrini.data.api.api_inter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object retrofit_Inter {

    val api by lazy {
        Retrofit.Builder()
            .baseUrl(urls_tamrini.baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api_inter::class.java)
    }
}