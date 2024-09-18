package com.example.jetpackshop.newProject.ui


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackshop.R
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BannerScreen() {
    val modifier = Modifier
    val content = LocalContext.current
    val gradient = Brush.linearGradient(
        colors = arrayListOf(
            Color(0xFFB226E1),
            Color(0xFFFC6603),
            Color(0xFF5995EE),
            Color(0xFF3D3535)
        )
    )


    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.banner_image), contentDescription = "",
            modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier
                .fillMaxWidth()
                .background(color = Color.White.copy(0.4f), RoundedCornerShape(15.dp))
                .wrapContentHeight()
                .align(Alignment.BottomCenter)

        ) {
            Text(
                text = "Enjoy the world of movies",
                modifier.padding(vertical = 25.dp),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                style = TextStyle(
                    fontSize = 34.sp,
                    shadow = Shadow(
                        color = Color(0xFFFC5A03),
                        offset = Offset(10.0f, 10f)
                    ),
                    fontFamily = FontFamily(Font(R.font.cinzel_decorative)),
                    textAlign = TextAlign.Center
                )
            )

            LaunchedEffect(Unit) {
                delay(2000L)
                Toast.makeText(content, "this is a Text", Toast.LENGTH_SHORT).show()
            }

            Button(
                onClick = { /*TODO*/ },
                modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(color = Color.Gray.copy(0.6f), RoundedCornerShape(12.dp))
                    .border(
                        BorderStroke(3.dp, gradient),
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "GETIN", fontSize = 30.sp,
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.cinzel_decorative)),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
            }

        }
    }

}

@SuppressLint("NewApi")
@Preview(showBackground = true)
@Composable
fun adawdd() {
    BannerScreen()
}