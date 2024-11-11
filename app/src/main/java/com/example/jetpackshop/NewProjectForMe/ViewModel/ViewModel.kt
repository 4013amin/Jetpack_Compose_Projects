package com.example.jetpackshop.NewProjectForMe.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.jetpackshop.NewProjectForMe.data.models.ModelsDataForMe
import com.example.jetpackshop.NewProjectForMe.data.utils.RetrodiInctanse
import com.example.jetpackshop.newProject.data.Models.Fields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.IOException

class ViewModel(application: Application) : AndroidViewModel(application) {
    val data = mutableStateOf<List<Fields>>(arrayListOf())


    fun sendUsers(image: MultipartBody.Part, username: String, password: String, context: Context) {
        viewModelScope.launch {
            val response = try {
                RetrodiInctanse.api.addUsers(username, password, image)
            } catch (e: IOException) {
                Toast.makeText(context, "this is error in Io", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(context, "this is error in HttpException", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                da.
            }

        }
    }

}