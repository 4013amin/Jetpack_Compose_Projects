package com.example.jetpackshop.NewProjectForMe.data.api

import com.example.jetpackshop.NewProjectForMe.data.models.ModelsDataForMe
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Apis {

    @POST("api/")
    suspend fun sendData(
        @Part image: MultipartBody.Part,
        @Part("username") username: String,
        @Part("password") password: String
    ): Response<ModelsDataForMe>

    @GET("api/")
    suspend fun getData(): Response<List<ModelsDataForMe>>
}
