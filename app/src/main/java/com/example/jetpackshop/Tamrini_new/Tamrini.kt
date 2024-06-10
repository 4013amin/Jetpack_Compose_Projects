package com.example.jetpackshop.Tamrini_new

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackshop.Tamrini_new.data.models_new.Models_new
import com.example.jetpackshop.Tamrini_new.data.models_new.Models_newItem
import com.example.jetpackshop.Tamrini_new.data.retrodit_new.Retrofit_new
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import com.example.jetpackshop.shop.single_row
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class Tamrini : androidx.activity.ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            show_data_retrofit()
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun show_data_retrofit() {
    var data by remember { mutableStateOf(listOf<Models_newItem>()) }
    val scrollState = rememberScrollState()
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
    lazycolumn(data = data, scroller = scrollState)
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
