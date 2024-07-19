package com.example.jetpackshop.Test

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api_Testi {
    @POST("/sendRequest/")
    suspend fun sendRequest(
        @Body users: List<UserModel>,
    ): Response<UserModel>
}
