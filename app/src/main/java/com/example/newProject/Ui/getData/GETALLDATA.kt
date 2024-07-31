@file:Suppress("DEPRECATION")

package com.example.newProject.Ui.getData

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.jetpackshop.Test.data.viewModel.ViewModel
import com.example.jetpackshop.Test.ui.Navigation
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.newProject.data.ViewModel.ViewModles

class GETALLDATA : androidx.activity.ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ScreenData()
        }
    }
}

@Composable
fun ScreenData(modifier: Modifier = Modifier.background(Color.White)) {
    val viewModel: ViewModles = viewModel()  // استفاده از ViewModel
    val data by viewModel.data
    val error by viewModel.showErrorText
    val scroller = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color.White)
            .verticalScroll(scroller),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error.isNotEmpty()) {
            Text(text = error, color = Color.Red)
        }

        data.forEach { item ->
            DataItem(title = item.title, body = item.body, id = item.id)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun DataItem(title: String, body: String, id: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = "Title: $title",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Body: $body",
                style = TextStyle(
                    color = Color.DarkGray
                )
            )
        }
    }
}
