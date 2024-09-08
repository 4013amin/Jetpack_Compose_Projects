package com.example.jetpackshop.ApiReady.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.Modifier
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackshop.ApiReady.data.models.WeatherResponse
import com.example.jetpackshop.ApiReady.viewModels.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = colorScheme.background) {
                    WeatherScreen()
                }
            }
        }
    }
}


@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = viewModel()
    val weather by viewModel.weather.collectAsState()

    var city by remember { mutableStateOf("Tehran") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter city") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.getWeather(city) }) {
            Text("Get Weather")
        }
        Spacer(modifier = Modifier.height(16.dp))
        weather?.let {
            WeatherDisplay(weatherResponse = it)
        }
    }
}

@Composable
fun WeatherDisplay(weatherResponse: WeatherResponse) {
    Column {
        Text(
            text = "Temperature: ${weatherResponse.main.temp}°C",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Humidity: ${weatherResponse.main.humidity}%",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Description: ${weatherResponse.weather.firstOrNull()?.description ?: "N/A"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun showWea() {
    WeatherScreen()
}