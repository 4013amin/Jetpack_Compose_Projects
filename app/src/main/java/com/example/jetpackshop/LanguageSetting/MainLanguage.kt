package com.example.jetpackshop.LanguageSetting

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.jetpackshop.R
import com.example.jetpackshop.Websocket.ui.MainNavigation
import com.example.jetpackshop.ui.theme.JetPackShopTheme

class MainLanguage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            showText()
        }
    }
}


@Composable
fun showText() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        item {
            Text(text = "Title :", fontStyle = FontStyle.Italic)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }

        items(10) {
            Text(
                text = stringResource(id = R.string.title),
                fontStyle = FontStyle.Italic,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}