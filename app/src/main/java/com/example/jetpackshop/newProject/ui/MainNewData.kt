package com.example.jetpackshop.newProject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackshop.navigations.nav
import com.example.jetpackshop.newProject.viewModel.ViewModelNew
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.itextpdf.layout.element.Text
import kotlinx.coroutines.launch

class MainNewData : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sendData(viewModel : ViewModelNew){
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var number by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Login", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                // Username input
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Number input
                TextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Number") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Submit button
                Button(
                    onClick = {
                        // Call ViewModel to send data to server
                        viewModel.sendNewData()
                    }
                ) {
                    Text("Submit")
                }
            }
        },
        sheetBackgroundColor = Color.LightGray,
        sheetElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Lottie animation at the top
            LottieAnimationView()

            Spacer(modifier = Modifier.height(16.dp))

            // Button to show Bottom Sheet
            Button(
                onClick = {
                    coroutineScope.launch {
                        sheetState.show() // Show the sheet
                    }
                }
            ) {
                Text("Open Login")
            }
        }
    }
}

@Composable
fun LottieAnimationView() {
    // Load the Lottie file
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("your_lottie_file.json"))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}