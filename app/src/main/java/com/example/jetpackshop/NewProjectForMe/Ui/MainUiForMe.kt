package com.example.jetpackshop.NewProjectForMe.Ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.result.contract.ActivityResultContracts
import com.example.jetpackshop.NewProjectForMe.ViewModel.ViewModel
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import java.io.File
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.FileOutputStream

class MainUiForMe : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                val viewModel: ViewModel = viewModel()
                getData(viewModel)
            }
        }
    }
}


//Send Data With photo

@Composable
fun UserForm(viewModel: ViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    // Register the image picker
    val getImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            // Convert Uri to Bitmap if needed
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input field for username
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input field for password
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { getImageLauncher.launch("image/*") }) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the selected image
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            imageUri?.let { uri ->
                val file = getFileFromUri(uri, context)
                val imagePart =
                    MultipartBody.Part.createFormData("image", file.name, file.asRequestBody())
                viewModel.senData(imagePart, username, password)
            }
        }) {
            Text("Submit")
        }
    }
}

fun getFileFromUri(uri: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val file = File(context.cacheDir, "image.jpg") // Temporary file
    contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return file
}


@Composable
fun getData(
    viewModel: ViewModel
) {
    val data = viewModel.data

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(2.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(data.value) {
            DataItem(
                imageUrl = it.image,
                username = it.username,
                password = it.password
            )
        }

    }

}

@Composable
fun DataItem(
    imageUrl: String,
    username: String,
    password: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.LightGray)
        .padding(16.dp)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Image display
        ImageLoader(
            imageUrl = imageUrl,
            modifier = Modifier
                .size(64.dp)
                .padding(end = 8.dp)
        )

        // User info
        Column {
            Text(text = "Username: $username", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Password: $password", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

// Helper function to load images
@Composable
fun ImageLoader(imageUrl: String, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(imageUrl)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
