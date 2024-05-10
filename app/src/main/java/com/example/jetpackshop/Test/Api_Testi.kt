package com.example.jetpackshop.Test

import android.view.Display.Mode
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api_Testi {

    @POST("send_request")
    suspend fun send_request(
        @Body users: Model_Testi,
    ): retrofit2.Response<Model_Testi>
}