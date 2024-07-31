package com.example.newProject.data.Apis

import com.example.newProject.data.Models.GetDataItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {


    @GET("posts/")
    suspend fun GetAll(): Response<List<GetDataItem>>
}
