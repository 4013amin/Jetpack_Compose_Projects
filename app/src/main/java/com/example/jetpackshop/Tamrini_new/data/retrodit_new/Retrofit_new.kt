package com.example.jetpackshop.Tamrini_new.data.retrodit_new

import com.example.jetpackshop.Tamrini_new.data.Api_new.Api_new
import com.example.jetpackshop.Tamrini_new.data.utils_new.utils_urls_new
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit_new {

    val api: Api_new by lazy {
        Retrofit.Builder()
            .baseUrl(utils_urls_new.urls)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api_new::class.java)
    }

}