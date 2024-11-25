package com.example.jetpackshop.Authentication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.launch

class Authentication : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                AuthenticationScreen()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var password by remember { mutableStateOf("") }
    var isAuthenticated by remember { mutableStateOf(false) }
    var showPasswordInput by remember { mutableStateOf(false) }

    // اطمینان از اینکه activity به FragmentActivity تبدیل شده است
    val activity = context as? FragmentActivity
    if (activity != null) {
        val biometricPrompt = BiometricPrompt(
            activity, // استفاده از FragmentActivity به جای ComponentActivity
            ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    isAuthenticated = true
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    isAuthenticated = false
                }
            }
        )

        // اطلاعات مربوط به BiometricPrompt
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("احراز هویت با اثر انگشت")
            .setSubtitle("برای ورود از اثر انگشت خود استفاده کنید")
            .setNegativeButtonText("لغو")
            .build()

        Scaffold {
            if (isAuthenticated) {
                Text(
                    text = "شما با موفقیت وارد شدید!",
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "برای ادامه یکی از روش‌های زیر را انتخاب کنید:")
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { biometricPrompt.authenticate(promptInfo) }) {
                        Text(text = "ورود با اثر انگشت")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { showPasswordInput = true }) {
                        Text(text = "ورود با رمز عبور")
                    }

                    if (showPasswordInput) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("رمز عبور") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            if (password == "1234") { // رمز عبور دلخواه
                                scope.launch { isAuthenticated = true }
                            } else {
                                scope.launch { isAuthenticated = false }
                            }
                        }) {
                            Text(text = "ورود")
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun showThis() {
    AuthenticationScreen()
}