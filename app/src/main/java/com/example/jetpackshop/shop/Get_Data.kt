package com.example.jetpackshop.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import com.example.jetpackshop.shop.data.utils.retrofit_instance
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

    Column(modifier = Modifier.fillMaxSize()) {
        DeleteAllButton { detectLongPress { (userList) } }
        my_lazyCoumn(userList = userList)
    }

}


@Composable
fun DeleteAllButton(onLongClick: () -> Unit) {
    Button(
        onClick = { /* Short click */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectLongPress { /* Long click */
                    onLongClick()
                }
            }
    ) {
        Text(text = "پاک کردن همه داده ها")
    }
}

@Composable
fun my_lazyCoumn(userList: List<Users_ModelItem>) {
    val navController = rememberNavController()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState()
    ) {
        items(userList.size) {
            CardItem(userList, it, navController)
        }
    }
}

@Composable
fun CardItem(user: List<Users_ModelItem>, index: Int, navController: NavController) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.White)
            .clickable {
                val name = user[index].name
                navController.navigate("Show_details?name=$name")
            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "${user[index].name}", fontSize = 18.sp)
            Text(text = "${user[index].price}", fontSize = 16.sp)
        }
    }
}


private fun detectLongPress(user: () -> Unit) {

    GlobalScope.launch(Dispatchers.IO) {
        val response = try {
            retrofit_instance.api.delete_all_users()
        } catch (e: IOException) {
            return@launch
        } catch (e: HttpException) {
            return@launch
        }

        if (response.isSuccessful && response.body() != null) {
            Log.e("Amin_delete", "request_is_ok ${response.message()}")

        } else {
            Log.e("Amin_delete", "Request failed: ${response.message()}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun show_get() {

}