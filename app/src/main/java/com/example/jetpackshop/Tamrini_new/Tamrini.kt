package com.example.jetpackshop.Tamrini_new

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackshop.Tamrini_new.data.models_new.Models_newItem
import com.example.jetpackshop.Tamrini_new.data.retrodit_new.Retrofit_new
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class Tamrini : androidx.activity.ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "show_data_retrofit") {
//        composable("show_data_retrofit") { show_data_retrofit(navController) }
//        composable("send_data_screen") { SendDataScreen(navController) }
//    }
    show_data_retrofit()
}

@Composable
fun SendDataScreen() {

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun show_data_retrofit() {
    var data by remember { mutableStateOf(listOf<Models_newItem>()) }
    var searchText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            val response = try {
                Retrofit_new.api.get_All_Data()
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                with(Dispatchers.Main) {
                    data = response.body()!!
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("+")
            }
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                // TextField برای ورودی جستجو
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text(text = "Search...") }
                )
                // فیلتر کردن داده‌ها براساس متن جستجو
                val filteredData = if (searchText.isEmpty()) {
                    data
                } else {
                    data.filter {
                        it.title.contains(searchText, ignoreCase = true) ||
                                it.body.contains(searchText, ignoreCase = true)
                    }
                }
                // نمایش لیست فیلتر شده
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredData) { user ->
                        show_data_ui(user)
                    }
                }
            }
        }
    )
}

@Composable
fun lazycolumn(data: List<Models_newItem>, scroller: ScrollState) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(data) { user ->
            show_data_ui(user)
        }
    }
}

@Composable
fun show_data_ui(user: Models_newItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Title: ${user.title}", fontSize = 20.sp, color = Color.Red)
            Text(text = "Description: ${user.body}", fontSize = 16.sp, color = Color.Black)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview(showBackground = true)
@Composable
fun show_Screen() {
    show_data_retrofit()
}
