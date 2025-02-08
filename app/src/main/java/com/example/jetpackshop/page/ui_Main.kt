package com.example.jetpackshop.page

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.jetpackshop.Websocket.ui.MainNavigation
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import java.lang.reflect.Modifier


class ui_Main : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                PageMain()
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


@Preview(showBackground = true)
@Composable
private fun ShowMAinPge() {
    PageMain()
}