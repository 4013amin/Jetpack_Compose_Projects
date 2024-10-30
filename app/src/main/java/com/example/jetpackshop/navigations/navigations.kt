package com.example.jetpackshop.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun nav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "A") {
            composable("A") {
            screen_A(navController)
        }

        composable("B") {
            screen_B(navController)
        }

        composable("C") {
            screenC(navController)
        }
    }
}


