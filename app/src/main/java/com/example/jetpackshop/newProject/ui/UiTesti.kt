package com.example.jetpackshop.newProject.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshop.newProject.viewModel.ViewModelNew

@Composable
fun MainUiData(viewModelNew: ViewModelNew = viewModel()) {

    val data = viewModelNew.data

    LaunchedEffect(Unit) {
        viewModelNew.getAllNewData()
    }


    LazyColumn() {
        items(data.value) {
            MainDataItem(username = it.username, name = it.name)
        }
    }

}

@Composable
fun MainDataItem(username: String, name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(color = Color.White)
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = username, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        }
    }

}
