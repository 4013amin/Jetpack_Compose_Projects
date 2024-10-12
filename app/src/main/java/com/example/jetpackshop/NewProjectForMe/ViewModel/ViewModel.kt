package com.example.jetpackshop.NewProjectForMe.ViewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.NewProjectForMe.data.models.ModelsDataForMe
import com.example.jetpackshop.NewProjectForMe.data.utils.RetrodiInctanse
import com.example.jetpackshop.newProject.data.Models.Fields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ViewModel(application: Application) : AndroidViewModel(application) {

    var data = mutableStateOf<List<ModelsDataForMe>>(arrayListOf())
    var number = mutableStateOf(0)

    fun sendData(image: MultipartBody.Part, username: String, password: String) {
        viewModelScope.launch {
            val response = try {
                RetrodiInctanse.api.SendDataForMe(image, username, password)
            } catch (e: Exception) {
                Log.e("Error", "Exception occurred: ${e.message}", e)
                return@launch
            }


            if (response.isSuccessful && response.body() != null) {
                data.value = arrayListOf(response.body()!!)
                Log.d("Response", response.body().toString())
            }
        }

    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                RetrodiInctanse.api.getData()
            }catch (e: Exception) {
                Log.e("Error", "Exception occurred: ${e.message}", e)
                return@launch
            }

            if (response.isSuccessful && response.body() != null){
                data.value = response.body()!!
            }
        }
    }


    fun AddNumber() {
        number.value += 1
    }

}