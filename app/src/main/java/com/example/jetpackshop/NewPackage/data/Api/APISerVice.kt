package com.example.jetpackshop.NewPackage.data.Api

import retrofit2.Response
import retrofit2.http.GET

interface APISerVice {

    @GET("Urls/get")
    suspend fun getData():Response<>

}