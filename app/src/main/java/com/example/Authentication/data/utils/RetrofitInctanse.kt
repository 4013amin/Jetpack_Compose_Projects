package com.example.Authentication.data.utils


import Api
import retrofit2.Retrofit

object RetrofitInctanse {


    const val url = "http://example"

    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .build()
            .create(Api::class.java)
    }

}