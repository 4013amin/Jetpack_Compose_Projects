package com.example.jetpackshop.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackshop.R
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import com.example.jetpackshop.shop.data.utils.retrofit_instance
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class Main : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                MyForm(LocalContext.current)
            }
        }
    }
}

@Composable
fun MyForm(context: Context) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(10.dp),
            ),
        Alignment.Center,

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(CutCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("نام") }
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("نام خانوادگی") }
                )
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("نام کالا") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("توضیحات") }
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("تاریخ") }
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("قیمت") }
                )

                OutlinedButton(
                    onClick = {
                        sendRequest(
                            name,
                            lastName,
                            productName,
                            description,
                            date,
                            price,
                            context
                        )

                    },

                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("ثبت کردن")
                }

                OutlinedButton(onClick = {
                    val intent = Intent(context, Get_Data::class.java)
                    // اجرای Intent برای انتقال به صفحه دیگر
                    context.startActivity(intent)
                }) {
                    Text(text = "نمایش کاربران", color = Color.Black)
                }

            }
        }
    }
}


private fun sendRequest(
    name: String,
    lastName: String,
    productName: String,
    description: String,
    date: String,
    price: String,
    context: Context
) {
    GlobalScope.launch(Dispatchers.IO) {
        val response = try {
            retrofit_instance.api.send_data(
                Users_ModelItem(
                    date = date,
                    description = description,
                    last_name = lastName,
                    name = name,
                    price = price,
                    product_name = productName,
                )
            )
        } catch (e: IOException) {
            Log.e("Amin", "IOException occurred: ${e.message}")
            return@launch
        } catch (e: HttpException) {
            Log.e("Amin", "HttpException occurred: ${e.message}")
            return@launch
        }

        if (response.isSuccessful && response.body() != null) {
            Log.i("Amin", "Request successful: ${response.message()}")
            val intent = Intent(context, Get_Data::class.java)
            // اجرای Intent برای انتقال به صفحه دیگر
            context.startActivity(intent)
            return@launch
        } else {
            Log.e("Amin", "Request failed: ${response.message()}")
            return@launch
        }
    }
}

//private fun sendRequest() {
//    GlobalScope.launch(Dispatchers.IO) {
//        val response = try {
//            retrofit_instance.api.send_data()
//        } catch (e: IOException) {
//            return@launch
//        } catch (e: HttpException) {
//            return@launch
//        }
//        if (response.isSuccessful && response.body() != null){
//
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun ui() {
    MyForm(LocalContext.current)
}