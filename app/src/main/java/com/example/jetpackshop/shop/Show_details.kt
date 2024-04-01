package com.example.jetpackshop.shop

import android.icu.text.ListFormatter.Width
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.jetpackshop.shop.data.utils.retrofit_instance
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class Show_details : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                
            }
        }
    }
}

@Composable
fun get_data_by_id() {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true){
        scope.launch (Dispatchers.IO){
            val response = try {
                retrofit_instance.api.get_data()
            }catch (e : IOException){
                return@launch
            }catch (e : HttpException){
                return@launch
            }

            if (response.isSuccessful && response != null){
                withContext(Dispatchers.Main){
//                    single_show_data()
                }
            }
        }
    }

}


