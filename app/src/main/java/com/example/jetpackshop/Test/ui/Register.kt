package com.example.jetpackshop.Test.ui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackshop.Test.data.model.Image
import com.example.jetpackshop.shop.Screen_Form
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.jetpackshop.Test.data.viewModel.ViewModel


class Register : androidx.activity.ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                val navController = rememberNavController()
                Navigation()
            }
        }
    }
}

@Composable
fun RegisterScreen(onImagePicked: (String) -> Unit, navController: NavController) {
    val viewModel: ViewModel = viewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }


    //GetImageForUsers
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = it.toString()
                onImagePicked(imageUri)
            }
        }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(15.dp)
                .background(Color.Cyan),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone Field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Image Picker Button
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Display selected image
            if (imageUri.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(
                        data = imageUri,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Register Button
            Button(
                onClick = { viewModel.sendRegister(username, password, phone) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Login Button
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            //Toast
            Text(
                text = viewModel.registerText.value,
                color = if (viewModel.registerText.value.contains("Registration Successful")) Color.Green else Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
            )
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel: ViewModel = viewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(15.dp)
                .background(Color.Cyan),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


            // Register Button
            Button(
                onClick = { viewModel.sendLogin(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Login Button
            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            //logout
            Button(
                onClick = { viewModel.sendLogout(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("logout")
            }

            Text(
                text = viewModel.logout.value,
                color = if (viewModel.logout.value.contains("login is ok")) Color.Green else Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
            )


            //Toast
            Text(
                text = viewModel.loginMassage.value,
                color = if (viewModel.loginMassage.value.contains("login is ok")) Color.Green else Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
            )
        }
    }
}

//navigation
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "register") {
        composable("register") {
            RegisterScreen(onImagePicked = { _ -> /* handle image pick */ }, navController)
        }

        composable("login") {
            LoginScreen(navController)
        }
    }
}
