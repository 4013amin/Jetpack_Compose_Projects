package com.example.jetpackshop.randomfact

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackshop.shop.data.models.Facts
import com.example.jetpackshop.shop.data.utils.retrofit_instance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

var facts = mutableStateOf(Facts())

class Random_fact : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            For_click()
        }
    }
}

@Composable
fun For_click() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(color = Color.White)
            .clickable { send_request() }
    ) {
        send_request()
        my_ui_facts(list_fact = facts)
    }
}


fun send_request() {
    GlobalScope.launch(Dispatchers.IO) {
        val response = try {
            retrofit_instance.api.getRandomFact()
        } catch (e: IOException) {
            return@launch
        } catch (e: HttpException) {
            return@launch
        }

        if (response.isSuccessful && response.body() != null) {
            withContext(Dispatchers.Main) {
                facts.value = response.body()!!
            }
        }
    }
}

@Composable
fun my_ui_facts(list_fact: MutableState<Facts>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp)
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(20)),

        ) {

        Text(text = list_fact.value.fact, fontSize = 20.sp)

    }
}

@Preview(name = "fact", showBackground = true)
@Composable
private fun show_fact() {
    my_ui_facts(facts)
}