package com.example.ptoject.data.ViewModles

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.IOException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.Test.data.utils.Utils
import com.example.ptoject.data.ModelsProjects.ModelProjects
import com.example.ptoject.data.UtilsProjects.UtilsRetrofit
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class ViewModelsProject(application: Application) : AndroidViewModel(application) {

    var registerText = mutableStateOf("")
    var modelProject = mutableStateOf<ModelProjects?>(null)

    fun getData(id: Int) {
        viewModelScope.launch {
            val response = try {
                UtilsRetrofit.api.getData(id)
            } catch (e: IOException) {
                registerText.value = "error is in Io"
                return@launch
            } catch (e: HttpException) {
                registerText.value = "error in http Response"
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                modelProject.value = response.body()!!
            }
        }

    }
}