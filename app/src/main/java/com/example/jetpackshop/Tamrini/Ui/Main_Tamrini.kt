package com.example.jetpackshop.Tamrini.Ui

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.widget.Scroller
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshop.Tamrini.ViewModels.ViewModelsTamrini
import com.example.jetpackshop.Tamrini.data.api.api_inter
import com.example.jetpackshop.Tamrini.data.models.Models_Tamrini
import com.example.jetpackshop.Tamrini.data.models.Models_TamriniItem
import com.example.jetpackshop.Tamrini.data.utils.retrofit_Inter
import com.example.jetpackshop.Tamrini_new.lazycolumn
import com.example.jetpackshop.shop.data.models.Facts
import com.example.jetpackshop.shop.data.models.Users_ModelItem
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.google.firebase.database.core.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class Main_Tamrini : androidx.activity.ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                ShowGEtData()
            }
        }
    }
}


@Composable
fun ShowGEtData(viewModelsTamrini: ViewModelsTamrini = viewModel()) {

    val data = viewModelsTamrini.data
    val content = LocalContext.current

    LaunchedEffect(Unit) {
        viewModelsTamrini.getData()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModelsTamrini.sendData("this is testi is Amin", "4013_amin", content)
                },
                containerColor = Color(0xFFFF5722)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color.White)
            ) {
                items(data.value) { item ->
                    GEtDataItem(
                        body = item.body,
                        title = item.title,
                        userId = item.userId,
                        delete = {
                            viewModelsTamrini.deleteData(item.id, content)
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun GEtDataItem(body: String, title: String, userId: Int, delete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // Adds padding around the card for spacing
            .clip(RoundedCornerShape(4.dp))
            .clickable { delete() }
    ) {
        // محتوا داخل کارت
        Text(
            text = "Title: $title",
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = body,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(8.dp)
        )

    }

    // Adds some vertical space between cards
    Spacer(modifier = Modifier.height(16.dp))
}


@Preview(showBackground = true)
@Composable
fun showdaw() {
    ShowGEtData()
}


//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//@Composable
//fun Get_data_tamrini() {
//    var userlist by remember {
//        mutableStateOf(listOf<Models_TamriniItem>())
//    }
//    var scope = rememberCoroutineScope()
//    var scroller = rememberScrollState()
//
//    LaunchedEffect(key1 = true) {
//        scope.launch(Dispatchers.IO) {
//            var response = try {
//                retrofit_Inter.api.get_data_tamrini()
//            } catch (e: IOException) {
//                return@launch
//            } catch (e: HttpException) {
//                return@launch
//            }
//
//            if (response.isSuccessful && response.body() != null) {
//                userlist = response.body()!!
//            }
//        }
//    }
//    single_items(userlis = userlist)
//}
//
////@Composable
////fun lazy(userlis: List<Models_TamriniItem>, scroller: ScrollState) {
////    LazyColumn(modifier = Modifier.fillMaxWidth()) {
////        items(userlis.size) {
////            single_items(userlis)
////        }
////    }
////}
//
//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//@Composable
//fun single_items(userlis: List<Models_TamriniItem>) {
//
//    Button(
//        onClick = { delete_button() }, modifier = Modifier
//            .fillMaxWidth()
//            .padding(15.dp)
//            .background(color = Color.Green), shape = RoundedCornerShape(15.dp)
//    ) {
//        Text(text = "Delete All Data .....", fontSize = 18.sp)
//    }
//
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth(),
//        state = rememberLazyListState()
//    ) {
//        items(userlis) { user ->
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(color = Color.White)
//                    .padding(5.dp)
//            ) {
//                Text(text = "Title : ${user.title}", fontSize = 18.sp, color = Color.Black)
//            }
//        }
//    }
//}
//
//
//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//private fun delete_button() {
//    GlobalScope.launch(Dispatchers.IO) {
//        var response = try {
//            retrofit_Inter.api.delete_data()
//        } catch (e: IOException) {
//            return@launch
//        } catch (e: HttpException) {
//            return@launch
//        }
//        if (response.isSuccessful && response.body() != null) {
//            with(Dispatchers.Main) {
//                users.value = response.body()!!
//            }
//        }
//    }
//}
