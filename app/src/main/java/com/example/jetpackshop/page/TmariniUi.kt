package com.example.jetpackshop.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackshop.R

@Preview(showBackground = true)
@Composable
fun Ui() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {


        Column(Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = null,
                modifier = Modifier
                    .padding(15.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Text(
                    text = "username : ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "4013_amin",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,

                    )
            }


            boxs2()


        }


    }
}

@Composable
fun boxs() {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .height(170.dp)
                .padding(end = 12.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#37c9bb")),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.btn_1),
                contentDescription = null,
                modifier = Modifier
                    .height(66.dp)
                    .width(66.dp)
            )

            Text(
                text = "Developing ", fontSize = 18.sp,
                modifier = Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }


        Column(
            modifier = Modifier
                .weight(0.5f)
                .height(170.dp)
                .padding(end = 12.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#37c9bb")),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.btn_1),
                contentDescription = null,
                modifier = Modifier
                    .height(66.dp)
                    .width(66.dp)
            )

            Text(
                text = "Developing ", fontSize = 18.sp,
                modifier = Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


@Composable
fun boxs2() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(0.5f)
                .height(170.dp)
                .padding(end = 12.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#37c9bb")),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.btn_1),
                contentDescription = null,
                modifier = Modifier
                    .height(66.dp)
                    .width(66.dp)
            )

            Text(
                text = "Developing ", fontSize = 18.sp,
                modifier = Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }


        Column(
            modifier = Modifier
                .weight(0.5f)
                .height(170.dp)
                .padding(end = 12.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#37c9bb")),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.btn_1),
                contentDescription = null,
                modifier = Modifier
                    .height(66.dp)
                    .width(66.dp)
            )

            Text(
                text = "Developing ", fontSize = 18.sp,
                modifier = Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(0.5f)
                .height(170.dp)
                .padding(end = 12.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#37c9bb")),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.btn_1),
                contentDescription = null,
                modifier = Modifier
                    .height(66.dp)
                    .width(66.dp)
            )

            Text(
                text = "Developing ", fontSize = 18.sp,
                modifier = Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }


        Column(
            modifier = Modifier
                .weight(0.5f)
                .height(170.dp)
                .padding(end = 12.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#37c9bb")),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.btn_1),
                contentDescription = null,
                modifier = Modifier
                    .height(66.dp)
                    .width(66.dp)
            )

            Text(
                text = "Developing ", fontSize = 18.sp,
                modifier = Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

}