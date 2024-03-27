package com.example.jetpackshop.shop

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackshop.shop.data.models.Users_Model
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import com.example.jetpackshop.shop.data.utils.retrofit_instance
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class Get_Data : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                get_all_data_retrodit()
            }
        }
    }
}

@Composable
fun get_all_data_retrodit() {
    var userList by remember {
        mutableStateOf(listOf<Users_ModelItem>())
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.IO) {
            val response = try {
                retrofit_instance.api.get_data()
            } catch (e: IOException) {
                Log.e("Amin_getData", "${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Amin_getData", "${e.message}")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    userList = response.body()!!
                }
            }
        }

    }
    my_lazyCoumn(userList = userList)
}

@Composable
fun my_lazyCoumn(userList: List<Users_ModelItem>) {
    Card(modifier = Modifier.fillMaxWidth()) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(userList.size) {
                uiGet(userList = userList, index = it)
            }
        }
    }
}


@Composable
fun uiGet(userList: List<Users_ModelItem>, index: Int) {
    var counter = userList[index]
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "${counter.name}", fontSize = 18.sp)
            Text(text = "${counter.price}", fontSize = 18.sp)
            Divider(thickness = 2.dp, color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun show() {
    uiGet(userList = arrayListOf(), index = 0)
}