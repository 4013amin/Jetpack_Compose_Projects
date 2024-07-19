package com.example.jetpackshop.Test

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.IOException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException


class UserViewModel(application: Application) : AndroidViewModel(application) {
    var registrationResult = mutableStateOf("")
    var users = mutableListOf<UserModel>()

    fun sendRequest(username: String, password: String, phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserModel(username, password, phone)
            val response = try {
                Log.d("UserViewModel", "Sending request with user: $user")
                Base_testi.api.sendRequest(listOf(user))
            } catch (e: IOException) {
                Log.e("UserViewModel", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("UserViewModel", "HttpException: ${e.message}")
                return@launch
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception: ${e.message}")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                Log.d("UserViewModel", "Registration successful.")
                registrationResult.value = "Registration successful."
            } else {
                Log.e("UserViewModel", "Registration failed: ${response.errorBody()?.string()}")
            }
        }
    }
}