package com.example.Authentication.data.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Authentication.data.models.ProfileResponse
import com.example.Authentication.data.utils.RetrofitInctanse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File


class viewModel : ViewModel() {

    var registerResponse: String? = null
        private set

    var profileResponse: ProfileResponse? = null
        private set


    fun prepareImagePart(uri: Uri): MultipartBody.Part {
        val file = File(uri.path ?: "")
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }


    fun register(
        username: String,
        password: String,
        email: String,
        credit: Int,
        image: List<Uri>
    ) {
        viewModelScope.launch {
            try {
                val imageParts = image.map { prepareImagePart(it) }
                val response =
                    RetrofitInctanse.api.registerUser(username, password, email, credit, image)
                if (response.isSuccessful && response.body() != null) {
                    registerResponse = response.body()?.success
                } else {
                    registerResponse = response.body()?.error
                }
            } catch (e: HttpException) {
                registerResponse = "خطا: ${e.message}"
            }

        }
    }

    fun getProfile() {
        viewModelScope.launch {
            try {
                val response = RetrofitInctanse.api.getProfile()
                if (response.isSuccessful) {
                    profileResponse = response.body()
                }
            } catch (e: HttpException) {
                registerResponse = "خطا: ${e.message}"
            }
        }
    }

}
