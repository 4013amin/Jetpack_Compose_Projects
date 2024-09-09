package com.example.jetpackshop.Tamrini.data.api

import com.example.jetpackshop.Tamrini.data.models.Models_Tamrini
import com.example.jetpackshop.Tamrini.data.models.Models_TamriniItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface api_inter {

    @GET("posts")
    suspend fun get_data_tamrini(): Response<List<Models_TamriniItem>>


    @FormUrlEncoded
    @POST("/posts")
    suspend fun sendData(
        @Field("body") body: String,
        @Field("title") title: String
    ): Response<Models_TamriniItem>


    @DELETE("/posts/{id}")
    suspend fun delete_data(
        @Path("id") id: Int
    ): Response<Models_TamriniItem>


}