package com.example.jetpackshop.searchview

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SearchView : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val list = listOf(
                "java",
                "python",
                "javaScript",
                "C",
                "C#",
                "C+",
                "XML",
                "SQL",
                "HTML",
                "PHP",
                "CSS"
            )
            My_lazyColumn_SearchView(list)
        }
    }
}

@Composable
fun My_lazyColumn_SearchView(list: List<String>) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        items(items = list) { item ->
            single_item(item = item)
        }
    }
}

@Composable
fun single_item(item: String) {
    Column(modifier = Modifier.padding(15.dp)) {
        Text(text = item, fontSize = 30.sp)
        Divider(thickness = 2.dp, color = Color.Red)
    }
}

@Preview(showBackground = true)
@Composable
fun show_search() {
    single_item(item = "")
}