package com.example.ptoject.ui

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackshop.R
import com.example.jetpackshop.randomfact.send_request
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.ptoject.data.ViewModles.ViewModelsProject
import generatePdf
import openPdf
import java.io.File

class SendData : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
//                val viewModel: ViewModelsProject = viewModel()
//                GetData(viewModel, this)
                AnimationScreen()
            }
        }
    }
}


@Composable
fun GetData(viewModel: ViewModelsProject, context: Context) {
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
            id.toIntOrNull()?.let { viewModel.getData(it) }
        }) {
            Text("Get Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            modelProject?.let {
                val file = generatePdf(context, it, "report.pdf")
                Toast.makeText(context, "PDF saved to ${file.absolutePath}", Toast.LENGTH_LONG)
                    .show()
                openPdf(context, file)
            }
        }) {
            Text("Download PDF")
        }

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


@Composable
fun AnimationScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
    }
}
