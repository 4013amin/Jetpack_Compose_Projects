package com.example.jetpackshop.newProject.data.Api

import android.net.Uri
import com.example.jetpackshop.newProject.data.Models.Fields
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiNew {

    @Multipart
    @POST("app/sendNewData/")
    suspend fun sendNewData(
        @Part image: MultipartBody.Part,
        @Part("username") username: String,
        @Part("number") number: Int
    ): Response<List<Fields>>


    @GET("/app/getNewData/")
    suspend fun getDataNew(): Response<List<Fields>>


    @DELETE("app/deleteNewData/{id}")
    suspend fun deleteNewData(
        @Path("id") id: Int
    ): Response<List<Fields>>


    @PUT("")
    suspend fun updateNewData(
        @Path("id") id: Int
    ): Response<List<Fields>>


}