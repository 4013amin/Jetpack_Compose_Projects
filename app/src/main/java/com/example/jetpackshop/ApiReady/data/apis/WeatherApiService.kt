package com.example.jetpackshop.ApiReady.data.apis


import com.example.jetpackshop.ApiReady.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(@Query("q") city: String, @Query("appid") apiKey: String = "YOUR_API_KEY"): WeatherResponse

    companion object {
        fun create(): WeatherApi {
            return retrofit2.Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }
    }
}

