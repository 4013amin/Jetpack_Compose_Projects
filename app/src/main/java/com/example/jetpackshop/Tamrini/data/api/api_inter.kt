package com.example.jetpackshop.Tamrini.data.api

import com.example.jetpackshop.Tamrini.data.models.Models_Tamrini
import com.example.jetpackshop.Tamrini.data.models.Models_TamriniItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET

interface api_inter {

    @GET("posts")
    suspend fun get_data_tamrini(): Response<List<Models_TamriniItem>>

    @DELETE("DELETE")
    suspend fun delete_data(): Response<Models_Tamrini>

}