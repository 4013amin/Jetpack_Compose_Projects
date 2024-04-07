package com.example.jetpackshop.shop

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackshop.ui.theme.JetPackShopTheme

class navigations : androidx.activity.ComponentActivity() {}
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            JetPackShopTheme {
//                NavHost(navController = rememberNavController(), startDestination = "Get_Data") {
//                    composable("Show_details") {
//                        Show_details()
//                    }
//                }
//            }
//        }
//    }
//}