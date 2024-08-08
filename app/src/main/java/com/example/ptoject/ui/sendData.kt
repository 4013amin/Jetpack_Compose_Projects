package com.example.ptoject.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshop.randomfact.send_request
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.ptoject.data.ViewModles.ViewModelsProject

class SendData : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
                val viewModel: ViewModelsProject = viewModel()
                GetData(viewModel)
            }
        }
    }
}

@Composable
fun GetData(viewModel: ViewModelsProject) {
    var id by remember { mutableStateOf("") }
    val modelProject by viewModel.modelProject
    val registerText by viewModel.registerText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("Enter Post ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // Ensure ID is a valid integer before calling getData
            id.toIntOrNull()?.let { viewModel.getData(it) }
        }) {
            Text("Get Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = registerText)

        modelProject?.let {
            Column {
                Text("ID: ${it.id}")
                Text("Title: ${it.title}")
                Text("Body: ${it.body}")
                Text("User ID: ${it.userId}")
            }
        }
    }
}
