package com.example.Authentication.data.viewModel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Authentication.data.models.ProfileResponse
import com.example.Authentication.data.utils.RetrofitInctanse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class viewModel : ViewModel() {

    var registerResponse: String? = null
        private set

    var profileResponse: ProfileResponse? = null
        private set

    fun prepareImagePart(uri: Uri, context: Context): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = getFileNameFromUri(uri, context) ?: "image.jpg"
        val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", fileName, requestBody!!)
    }

    private fun getFileNameFromUri(uri: Uri, context: Context): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }

    fun register(
        context: Context,
        username: String,
        password: String,
        email: String,
        credit: Int,
        images: List<Uri>
    ) {
        viewModelScope.launch {
            try {
                val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
                val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
                val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                val creditBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), credit.toString())

                val imageParts = images.map { prepareImagePart(it, context) }

                val response = RetrofitInctanse.api.registerUser(
                    usernameBody,
                    passwordBody,
                    emailBody,
                    creditBody,
                    imageParts[0]
                )

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
