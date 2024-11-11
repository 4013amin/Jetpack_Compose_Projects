package com.example.jetpackshop.NewProjectForMe.data.api

import com.example.jetpackshop.NewProjectForMe.data.models.ModelsDataForMe
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Apis {

    @POST("")
    suspend fun addUsers(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part
    )

    @GET("")
    suspend fun getData(): Response<List<ModelsDataForMe>>


    @POST("")
    suspend fun loginUsers(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody
    )
}
