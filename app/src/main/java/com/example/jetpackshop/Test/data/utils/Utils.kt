package com.example.jetpackshop.Test.data.utils

import com.example.jetpackshop.Test.data.api.ApiProject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Utils {


    val api: ApiProject by lazy {
        Retrofit.Builder()
            .baseUrl(urlsNew.urlNew)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiProject::class.java)
    }


}