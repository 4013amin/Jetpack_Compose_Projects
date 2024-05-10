package com.example.jetpackshop.Test

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Utils_testi {

    val api: Api_Testi by lazy {
        Retrofit.Builder()
            .baseUrl(Base_testi.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api::class.java)
    }

}