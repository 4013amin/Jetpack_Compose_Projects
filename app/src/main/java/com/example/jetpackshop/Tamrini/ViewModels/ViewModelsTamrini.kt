package com.example.jetpackshop.Tamrini.ViewModels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.jetpackshop.Tamrini.data.api.api_inter
import com.example.jetpackshop.Tamrini.data.models.Models_Tamrini
import com.example.jetpackshop.Tamrini.data.models.Models_TamriniItem
import com.example.jetpackshop.Tamrini.data.utils.retrofit_Inter
import com.example.jetpackshop.Tamrini_new.data.utils_new.utils_urls_new
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

}