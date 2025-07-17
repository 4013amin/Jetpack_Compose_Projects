package com.example.jetpackshop.APITesti.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.jetpackshop.APITesti.data.models.OTPModel
import com.example.jetpackshop.APITesti.data.utils.Utils_APITest
import kotlinx.coroutines.launch
import okio.IOException

class ViewModelApi(application: Application) : AndroidViewModel(application) {

    var data = mutableStateOf<OTPModel?>(null)

    fun sendOTPReqest(phone_number: String) {
        viewModelScope.launch {
            val response = try {
                Utils_APITest.API.OTPRegister(phone_number)
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

    fun getAllProduct() {
        viewModelScope.launch {
            var response = try {
                Utils_APITest.API.getAllProduct()
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
}