package com.example.ptoject.data.Api_projects

import com.example.ptoject.data.ModelsProjects.ModelProjects
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface Api_Project {


    @GET("posts/{id}")
    suspend fun getData(
        @Path("id") id: Int
    ): retrofit2.Response<ModelProjects>
}