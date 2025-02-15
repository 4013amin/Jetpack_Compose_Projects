package com.example.Authentication.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import com.example.Authentication.data.shared.SharedPrefers
import com.example.Authentication.data.viewModel.viewModel
import com.example.jetpackshop.R
import com.example.jetpackshop.ui.theme.JetPackShopTheme


class MainUI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                NavigationSetup()
            }
        }
    }
}


@Composable
fun NavigationSetup() {
    val navController = androidx.navigation.compose.rememberNavController()

    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = "register"
    ) {
        composable("register") {
            RegisterScreen(navController)
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: viewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    var username by remember {
        mutableStateOf(SharedPrefers.getUsername(context))
    }

    var password by remember {
        mutableStateOf(SharedPrefers.getPassword(context))
    }

    var email by remember {
        mutableStateOf(SharedPrefers.getEmail(context))
    }
    var credit by remember {
        mutableStateOf(0)
    }
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            images = uris
        })
    var passwordVisibility by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ثبت آگهی جدید",
                            color = Color.White,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.background_color_2)
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .wrapContentHeight()
                    .padding(top = 25.dp)
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(value = username, onValueChange = { username = it }, placeholder = {
                    Text(text = "username")
                })

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = password, onValueChange = { password = it },
                    placeholder = {
                        Text(text = "password")
                    },
                    trailingIcon = {
                        if (passwordVisibility) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_password_24),
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_password_24),
                                contentDescription = null
                            )
                        }
                    },

                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = email, onValueChange = { email = it },
                    placeholder = {
                        Text(text = "email")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                )


                IconButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.padding(15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )

                }

                Button(
                    onClick = {
                        viewModel.register(
                            context,
                            username,
                            password,
                            email,
                            credit,
                            images
                        )

                        SharedPrefers.saveUsername(context, username)
                        SharedPrefers.savePassword(context, password)
                        SharedPrefers.saveEmail(context, email)


                        navController.navigate("profile")

                    },
                    modifier = Modifier
                        .width(200.dp)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    )
                ) {
                    Text(
                        text = "ثبت نام",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                }

            }

        }
    )
}

@Composable
fun ProfileScreen(viewModel: viewModel = viewModel()) {
    val profile = viewModel.profileResponse

    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }

    if (profile != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // نمایش تصویر
            Image(
                painter = rememberImagePainter("YOUR_BASE_URL/${profile.image}"),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // نمایش اعتبار
            Text(
                text = "اعتبار: ${profile.credit}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // نمایش جنسیت
            val genderText = when (profile.gender) {
                1 -> "مرد"
                2 -> "خانم"
                else -> "نامشخص"
            }

            Text(
                text = "جنسیت: $genderText",
                fontSize = 18.sp
            )
        }
    }

}

