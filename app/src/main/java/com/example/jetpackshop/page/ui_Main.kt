package com.example.jetpackshop.page

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.jetpackshop.R
import com.example.jetpackshop.Websocket.ui.MainNavigation
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import java.lang.reflect.Modifier


class ui_Main : FragmentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                PreviewMainPage()
            }
        }
    }
}

@Composable
fun PageMain() {
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        var ShowDialog by remember {
            mutableStateOf(false)
        }

        var checked by remember {
            mutableStateOf(false)
        }

        Box(
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "open Dialog", modifier = androidx.compose.ui.Modifier.clickable {
                ShowDialog = true
            })
        }
        if (ShowDialog) {
            ShowDialog(
                onDismissRequest = { ShowDialog = false },
                confirmButton = { ShowDialog = false },
                onDismissButton = { ShowDialog = false }
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = androidx.compose.ui.Modifier.padding(15.dp)
        ) {
            Text(text = "This a Check Box")
            Checkbox(checked = checked, onCheckedChange = { checked = it })
        }

        ElevatedButton(
            onClick = { ShowDialog = true },
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(15.dp),
            enabled = checked
        ) {
            Text("Elevated")
        }

    }
}

@Composable
fun ShowDialog(
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit,
    onDismissButton: () -> Unit
) {
    AlertDialog(
        icon = {
            Icons.Default.Person
        },
        title = { Text(text = "Dialog Title") },
        text = { Text(text = "Dialog Content") },
        onDismissRequest = { onDismissRequest() },

        confirmButton = {
            TextButton(onClick = { confirmButton() }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissButton() }) {
                Text(text = "Dismiss")
            }
        }
    )
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PreviewMainPage() {
    var ShowDialog by remember {
        mutableStateOf(false)
    }
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val scroller = rememberScrollState()
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(color = Color.White),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(Icons.Filled.MoreVert, contentDescription = null)
                },
                title = {
                    Text(
                        text = "This is Top Bar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF80A4EC),
                )
            )
        },

        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                ShowDialog = true
            }) {
                Icon(Icons.Filled.Person, contentDescription = null)
            }
        },
        content = {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .background(color = Color.White)
                    .verticalScroll(scroller),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_image),
                    contentDescription = null
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = androidx.compose.ui.Modifier.padding(15.dp),
                    placeholder = {
                        Text(text = "Username")
                    },
                    isError = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = androidx.compose.ui.Modifier.padding(15.dp),
                    placeholder = {
                        Text(text = "password")
                    },
                    isError = false,
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (passwordVisible) Icons.Filled.Person else Icons.Filled.ArrowDropDown
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
        }
    )

    if (ShowDialog) {
        ShowDialog(
            onDismissRequest = { ShowDialog = false },
            confirmButton = { ShowDialog = false },
            onDismissButton = { ShowDialog = false }
        )
    }
}

@Composable
fun ProductItem() {

}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun ShowMAinPge() {
    PreviewMainPage()
}