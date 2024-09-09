package com.example.jetpackshop.Tamrini.ViewModels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.Tamrini.data.models.Models_TamriniItem
import com.example.jetpackshop.Tamrini.data.utils.retrofit_Inter
import com.itextpdf.io.exceptions.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ViewModelsTamrini(application: Application) : AndroidViewModel(application) {

    var data = mutableStateOf<List<Models_TamriniItem>>(arrayListOf())
    fun getData() {
        viewModelScope.launch {
            val response = try {
                retrofit_Inter.api.get_data_tamrini()
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

    fun sendData(body: String, title: String, context: Context) {
        viewModelScope.launch {
            val response = try {
                retrofit_Inter.api.sendData(body, title)
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val newDta = response.body()!!
                data.value = data.value + newDta

                // نمایش Toast پس از موفقیت
                Toast.makeText(context, "Data sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                // نمایش Toast در صورت عدم موفقیت
                Toast.makeText(context, "Failed to send data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun deleteData(id: Int, context: Context) {
        viewModelScope.launch {
            val response = try {
                retrofit_Inter.api.delete_data(id)
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                data.value = data.value.filter { it.id != id }

                Toast.makeText(context, "this is deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "this is error in deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


