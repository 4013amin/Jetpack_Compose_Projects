package com.example.jetpackshop.Tamrini.data.api

import com.example.jetpackshop.Tamrini.data.models.Models_Tamrini
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET

interface api_inter {

    @GET("posts")
    suspend fun get_data_tamrini(): Response<Models_Tamrini>

    @DELETE("DELETE")
    suspend fun delete_data(): Response<Models_Tamrini>

}