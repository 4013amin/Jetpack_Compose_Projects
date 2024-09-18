package com.example.jetpackshop.newProject.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.newProject.data.Models.Fields
import com.example.jetpackshop.newProject.data.Utils.RetrofitNewInctance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException

class ViewModelNew(application: Application) : AndroidViewModel(application) {

    var data = mutableStateOf<List<Fields>>(arrayListOf())

    fun sendNewData(image: MultipartBody.Part, username: String, number: Int) {
        viewModelScope.launch {
            val response = try {
                RetrofitNewInctance.api.sendNewData(image, username, number)
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                data.value = response.body()!!
                Log.d("Response", response.body().toString())

            }
        }
    }


    fun getAllNewData() {
        viewModelScope.launch {
            val response = try {
                RetrofitNewInctance.api.getDataNew()
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                data.value = response.body()!!
            }
        }
    }


    fun deleteNewData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitNewInctance.api.deleteNewData(id)
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                data.value = data.value.filter { it.id != id }
            }

        }
    }


    fun updateNewData(id: Int) {
        viewModelScope.launch {
            val response = try {
                RetrofitNewInctance.api.updateNewData(id)
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                data.value = data.value.filter { it.id != id }
            }
        }

    }

}