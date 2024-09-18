package com.example.jetpackshop.newProject.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackshop.R
import com.example.jetpackshop.navigations.nav
import com.example.jetpackshop.newProject.data.Models.Fields
import com.example.jetpackshop.newProject.viewModel.ViewModelNew
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.itextpdf.layout.element.Text
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class MainNewData : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
////                sendNewData()
//                getDataNew()
                BannerScreen()
            }
        }
    }
}


fun uriToMultipartBody(context: Context, imageUri: Uri, partName: String): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val file = File(context.cacheDir, "temp_image.jpg")
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()

    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}


@Composable
fun sendNewData(viewModelNew: ViewModelNew = viewModel()) {
    var username by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(color = Color.White)
    ) {
        LottieAnimationView()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Image picker
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Gray, shape = CircleShape)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    val bitmap = context.contentResolver.openInputStream(selectedImageUri!!)
                        ?.use {
                            android.graphics.BitmapFactory.decodeStream(it)
                        }
                    bitmap?.let {
                        androidx.compose.foundation.Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Selected Image",
                            modifier = Modifier.size(120.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Toast.makeText(context, "Please select an image", Toast.LENGTH_LONG).show()
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = number,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        number = input
                    }
                },
                label = { Text(text = "Enter a number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                val numberAsInt = number.toIntOrNull()
                if (numberAsInt != null && selectedImageUri != null) {
                    val imagePart = uriToMultipartBody(context, selectedImageUri!!, "image")
                    viewModelNew.sendNewData(imagePart, username, numberAsInt)
                } else {
                    Toast.makeText(context, "Please enter valid data", Toast.LENGTH_LONG).show()
                }
            }) {
                Text(text = "Submit")
            }
        }
    }
}


@Composable
fun getDataNew(viewModelNew: ViewModelNew = viewModel()) {
    val userDataList by viewModelNew.data

    LaunchedEffect(Unit) {
        viewModelNew.getAllNewData()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(userDataList) { userData ->
            UserCard(
                userData,
                delete = {
                    viewModelNew.deleteNewData(userData.id)
                    viewModelNew.updateNewData(userData.id)
                })
        }
    }
}

@Composable
fun UserCard(userData: Fields, delete: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { delete() },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "User Image",
                modifier = Modifier.size(50.dp)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = userData.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                Text(
                    text = userData.number.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Composable
fun LottieAnimationView() {
    // Load the Lottie file
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loginanimation))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
