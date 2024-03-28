package com.example.jetpackshop.searchview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity

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
            MyLazyColumnSearchView(list)
        }
    }
}

@Composable
fun MyLazyColumnSearchView(list: List<String>, modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        SearchField(searchText = searchText, onSearchTextChanged = { searchText = it })
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = list.filter { it.contains(searchText, ignoreCase = true) }) { item ->
                SingleItem(item = item)
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(searchText: String, onSearchTextChanged: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = { onSearchTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
        placeholder = {
            Text(
                "Search here...",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Black)
            )
        },
        colors = TextFieldDefaults.textFieldColors(Color.Black),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun SingleItem(item: String) {
    Text(
        text = item,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp),
        color = Color.Black
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewSearchView() {
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
    MyLazyColumnSearchView(list)
}