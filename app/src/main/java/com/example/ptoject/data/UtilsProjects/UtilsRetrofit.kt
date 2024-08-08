package com.example.ptoject.data.UtilsProjects

import com.example.ptoject.data.Api_projects.Api_Project
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UtilsRetrofit {

    val api by lazy {
        Retrofit.Builder()
            .baseUrl(UrlsProject.urls)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api_Project::class.java)
    }

}