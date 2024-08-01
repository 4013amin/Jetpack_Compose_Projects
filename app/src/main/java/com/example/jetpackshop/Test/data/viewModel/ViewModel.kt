package com.example.jetpackshop.Test.data.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.IOException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.Test.data.api.ApiProject
import com.example.jetpackshop.Test.data.model.UsersModelsNew
import com.example.jetpackshop.Test.data.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException


class ViewModel(application: Application) : AndroidViewModel(application) {


    var registerText = mutableStateOf("")
    var loginMassage = mutableStateOf("")
    var logout = mutableStateOf("")
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

    fun sendLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var response = try {
                Utils.api.sendLogin(username, password)
            } catch (e: IOException) {
                Log.e("IoExceptions", "this is error is IoExceptions", e)
                loginMassage.value = "this is error for IoExceptions"
                return@launch
            } catch (e: HttpException) {
                Log.e("HttpIoExceptions", "this is a error for HttpExceptions", e)
                loginMassage.value = "this is error for HttpExceptions"
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                users.value = arrayListOf(response.body()!!)
                loginMassage.value = "login is ok"
            } else {
                loginMassage.value = "login is not Ok"
            }
        }
    }

    fun sendLogout(username: String, password: String) {
        viewModelScope.launch {
            var response = try {
                Utils.api.sendLogout(username, password)
            } catch (e: IOException) {
                Log.e("IoException", "${e.message}")
                logout.value = "${e.message}"
                return@launch
            } catch (e: HttpException) {
                Log.e("HttpException", "${e.message}")
                logout.value = "${e.message}"
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                users.value = arrayListOf(response.body()!!)
                logout.value = "logout is ok"
            } else {
                logout.value = "logout is not ok"
            }
        }
    }
}