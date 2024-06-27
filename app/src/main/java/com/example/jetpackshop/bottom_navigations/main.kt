package com.example.jetpackshop.bottom_navigations

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.example.jetpackshop.shop.MyForm
import com.example.jetpackshop.ui.theme.JetPackShopTheme

class main : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {

            }
        }
    }
}