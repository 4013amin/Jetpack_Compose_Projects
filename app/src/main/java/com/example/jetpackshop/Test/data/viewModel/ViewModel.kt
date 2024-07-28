package com.example.jetpackshop.Test.data.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.IOException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.Test.data.model.UsersModelsNew
import com.example.jetpackshop.Test.data.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.HttpException


class ViewModel(application: Application) : AndroidViewModel(application) {


    var registerText = mutableStateOf("")
    val users = mutableStateOf<List<UsersModelsNew>>(emptyList())

    //register
    fun sendRegister(username: String, password: String, phone: String) {

        viewModelScope.launch {
            var response = try {

                Utils.api.sendRegister(username, password, phone)
            } catch (e: IOException) {
                Log.e("IoException", "IoException", e)
                registerText.value == "error is a IOException"
                return@launch
            } catch (e: HttpException) {
                Log.e("HttpError", "This is error for http", e)
                registerText.value = "error is a Http response"
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                users.value = listOf(response.body()!!)
                registerText.value = "Registration Successful"
            } else {
                registerText.value = "Registration Failed"
            }

        }
    }
}