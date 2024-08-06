package com.example.jetpackshop.Test.data.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.IOException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.Tamrini_new.data.utils_new.utils_urls_new
import com.example.jetpackshop.Test.data.api.ApiProject
import com.example.jetpackshop.Test.data.model.UsersModelsNew
import com.example.jetpackshop.Test.data.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File


class ViewModel(application: Application) : AndroidViewModel(application) {


    var registerText = mutableStateOf("")
    var loginMassage = mutableStateOf("")
    var logout = mutableStateOf("")
    val users = mutableStateOf<List<UsersModelsNew>>(emptyList())

    //register
    fun sendRegister(username: String, password: String, phone: String, imageUri: String) {
        viewModelScope.launch {
            try {
                val file = File(imageUri)
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                val usernamePart = RequestBody.create(MultipartBody.FORM, username)
                val passwordPart = RequestBody.create(MultipartBody.FORM, password)
                val phonePart = RequestBody.create(MultipartBody.FORM, phone)

                val response = Utils.api.sendRegister(
                    body,
                    usernamePart,
                    passwordPart,
                    phonePart
                )
                if (response.isSuccessful) {
                    registerText.value = "Registration Successful"
                } else {
                    registerText.value = "Registration Failed"
                }
            } catch (e: Exception) {
                registerText.value = "Error: ${e.message}"
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