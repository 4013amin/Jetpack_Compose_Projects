package com.example.jetpackshop.APITesti.data.apis

import com.example.jetpackshop.APITesti.data.models.OTPModel
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Part

interface APITesti {

    @POST("/api/OTP_register/")
    suspend fun OTPRegister(
        @Part("phone_number") phone_number: String,
    ): Response<OTPModel>


}