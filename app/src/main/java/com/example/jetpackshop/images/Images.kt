package com.example.jetpackshop.images

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackshop.R
import com.example.jetpackshop.ui.theme.JetPackShopTheme

class Images : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        }
    }
}

@Composable
fun images() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .clip(RoundedCornerShape(15.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.images),
            modifier = Modifier
                .fillMaxWidth()
                .clip(CutCornerShape(20.dp))
                .border(2.dp, color = Color.Red, shape = CutCornerShape(20.dp)),
            contentDescription = "image",
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.size(30.dp))

        Image(
            painter = painterResource(id = R.drawable.images),
            modifier = Modifier
                .fillMaxWidth()
                .clip(CutCornerShape(20.dp))
                .border(2.dp, color = Color.Red, shape = CutCornerShape(20.dp)),
            contentDescription = "image",
            contentScale = ContentScale.Fit,

            )

    }
}

@Preview(showBackground = true)
@Composable
fun show_images() {
    images()
}