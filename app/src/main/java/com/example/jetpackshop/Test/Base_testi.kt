package com.example.jetpackshop.Test

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Base_testi {

    val retrofit = Retrofit.Builder()
        .baseUrl(Utils_testi.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(Api_Testi::class.java)

}