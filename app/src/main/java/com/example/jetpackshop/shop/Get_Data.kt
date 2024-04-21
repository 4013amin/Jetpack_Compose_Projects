package com.example.jetpackshop.shop

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackshop.shop.data.models.Users_Model
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import com.example.jetpackshop.shop.data.utils.retrofit_instance
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

var delete_users = mutableStateOf(Users_Model())

class Get_Data : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {

                get_data_retrofit()
            }
        }
    }
}

@Composable
fun get_data_retrofit() {
    var userlist by remember {
        mutableStateOf(listOf<Users_ModelItem>())
    }

    val scope = rememberCoroutineScope()
    var scroller = rememberScrollState()

    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.IO) {
            val response = try {
                retrofit_instance.api.get_data()
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    userlist = response.body()!!
                }
            }
        }
    }

    lazycolumn(userlist = userlist, scroller = scroller)
}

@Composable
fun lazycolumn(userlist: List<Users_ModelItem>, scroller: ScrollState) {
    Button(
        onClick = { deleteRetrofit() }, modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(color = Color.Gray),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(text = "DELETE ALl")
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(userlist) { user ->
            single_row(user)
        }
    }
}


@Composable
fun single_row(user: Users_ModelItem) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(color = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Username: ${user.name}", fontSize = 20.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Email: ${user.price}", fontSize = 16.sp, color = Color.Black)
    }
}

private fun deleteRetrofit() {

    GlobalScope.launch(Dispatchers.IO) {
        val response = try {
            retrofit_instance.api.delete_all_users()
        } catch (e: IOException) {
            return@launch
        } catch (e: HttpException) {
            return@launch
        }
        if (response.isSuccessful && response.body() != null) {
            delete_users.value = response.body()!!
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun show_get() {
    lazycolumn(userlist = arrayListOf(), scroller = ScrollState(1))
}