package com.example.jetpackshop.shop

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import com.example.jetpackshop.R
import com.example.jetpackshop.navigations.nav
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
                MyForm()
            }
        }
    }
}

@Composable
fun MyForm() {
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
                color = colorResource(id = R.color.white_new),
                shape = RoundedCornerShape(10.dp),
            )
        ,
        Alignment.Center,

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
                label = { Text("Name") }
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") }
            )
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") }
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") }
            )
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") }
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") }
            )

            Button(
                onClick = {
                    sendRequest(
                        name,
                        lastName,
                        productName,
                        description,
                        date,
                        price
                    )
                },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Submit")
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
    price: String
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
                    product_name = productName
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
        } else {
            Log.e("Amin", "Request failed: ${response.message()}")
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
    MyForm()
}