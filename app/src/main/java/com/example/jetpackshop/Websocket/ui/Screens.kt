package com.example.jetpackshop.Websocket.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.jetpackshop.Test.data.viewModel.ViewModel

@Composable
fun getUsersUi(viewModel: ViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
//        items() {
//
//        }
    }
}


@Composable
fun UsersItem(modifier: Modifier = Modifier) {

}