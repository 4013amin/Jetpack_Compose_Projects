package com.example.jetpackshop.Websocket.data.Api

import com.example.jetpackshop.Tamrini.data.models.Models_TamriniItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface apiwebsockt {

    @DELETE("/posts/{id}")
    suspend fun delete_data(
        @Path("id") id: Int
    ): Response<Models_TamriniItem>

}