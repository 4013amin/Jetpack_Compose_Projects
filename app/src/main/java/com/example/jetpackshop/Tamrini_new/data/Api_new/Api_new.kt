package com.example.jetpackshop.Tamrini_new.data.Api_new

import com.example.jetpackshop.Tamrini_new.data.models_new.Models_new
import okhttp3.Response
import retrofit2.http.GET

interface Api_new {

    @GET("posts")
    suspend fun get_All_Data(): retrofit2.Response<Models_new>

}