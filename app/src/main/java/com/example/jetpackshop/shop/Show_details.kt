package com.example.jetpackshop.shop

import android.icu.text.ListFormatter.Width
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
                val navController = rememberNavController()
                ShowDetails(navController = navController)
            }
        }
    }
}

@Composable
fun ShowDetails(navController: NavController) {
    val name = navController.currentBackStackEntry
        ?.arguments?.getString("name") ?: ""

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Name: $name", fontSize = 18.sp, modifier = Modifier.padding(16.dp))
    }
}


