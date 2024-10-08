package com.example.jetpackshop.NewProjectForMe.data.api

import com.example.jetpackshop.NewProjectForMe.data.models.ModelsDataForMe
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Apis {
    @Multipart
    @POST("app/sendData/")
    suspend fun SendDataForMe(
        @Part image: MultipartBody.Part,
        @Part("username") username: String,
        @Part("password") password: String
    ): Response<ModelsDataForMe>
}
