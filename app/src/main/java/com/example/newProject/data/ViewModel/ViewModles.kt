package com.example.newProject.data.ViewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.IOException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newProject.data.Models.GetDataItem
import com.example.newProject.data.Utils.retrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ViewModles(application: Application) : AndroidViewModel(application) {


    var showErrorText = mutableStateOf("")
    var data = mutableStateOf<List<GetDataItem>>(emptyList())

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = retrofitInstance.api.GetAll()
                if (response.isSuccessful && response.body() != null) {
                    data.value = response.body()!!
                } else {
                    showErrorText.value = "Error: ${response.message()}"
                }
            } catch (e: IOException) {
                Log.e("IOException", "IOException occurred", e)
                showErrorText.value = "Network error: ${e.message}"
            } catch (e: HttpException) {
                Log.e("HttpException", "HttpException occurred", e)
                showErrorText.value = "Server error: ${e.message()}"
            } catch (e: Exception) {
                Log.e("Exception", "Exception occurred", e)
                showErrorText.value = "Unexpected error: ${e.message}"
            }
        }
    }
}