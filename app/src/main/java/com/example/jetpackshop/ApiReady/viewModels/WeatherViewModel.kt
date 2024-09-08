package com.example.jetpackshop.ApiReady.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.ApiReady.data.apis.WeatherApi
import com.example.jetpackshop.ApiReady.data.models.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository() // یا از Dependency Injection استفاده کنید

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    fun getWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.getWeather(city)
                _weather.value = response
            } catch (e: Exception) {
                // Handle exceptions, maybe show an error message
                e.printStackTrace()
            }
        }
    }
}




class WeatherRepository {
    private val api = WeatherApi.create() // یا از Dependency Injection استفاده کنید

    suspend fun getWeather(city: String): WeatherResponse {
        return api.getWeather(city)
    }
}

