package com.example.jetpackshop.shop.data.Apis

import com.example.jetpackshop.shop.data.models.Facts
import com.example.jetpackshop.shop.data.models.Users_Model
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @POST("/app/create_user")
    suspend fun send_data(
        @Body users: Users_ModelItem,
    ): Response<Users_ModelItem>


    @GET("/app/get_all_users")
    suspend fun get_data(): Response<Users_Model>


    @GET()
    suspend fun get_data_by_id(
        @Path("id") id: Int,
    ): Response<Users_ModelItem>

    @DELETE("/app/delete_all_user")
    suspend fun delete_all_users(): Response<Users_Model>


    //Random_fact
    @GET("fact")
    suspend fun getRandomFact(): Response<Facts>

}