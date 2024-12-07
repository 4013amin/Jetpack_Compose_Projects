package com.example.jetpackshop.question

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.itextpdf.layout.element.Text
import kotlin.random.Random

class question : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowQuestion()
        }
    }
}

@Composable
fun ShowQuestion() {
    val randomNames = arrayOf("Amin", "Jafar", "Mamd", "Ahmad", "Niloofar")
    val selectedName = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Button(onClick = {
            selectedName.value = randomNames.random()
        }) {
            androidx.compose.material3.Text(text = "Click Here")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (selectedName.value.isNotEmpty())
                "Selected Name: ${selectedName.value}"
            else "No name selected yet",
            modifier = Modifier.padding(8.dp)
        )
    }
}
