package com.example.ptoject.ui

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshop.R
import com.example.jetpackshop.Tamrini_new.Tamrini
import com.example.jetpackshop.randomfact.send_request
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.ptoject.data.ViewModles.ViewModelsProject
import generatePdf
import openPdf
import java.io.File
import java.util.Calendar

class SendData : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
                val viewModel: ViewModelsProject = viewModel()
                GetData(viewModel, this)
//                tamrini()
            }
        }
    }
}


@Composable
fun GetData(viewModel: ViewModelsProject, context: Context) {
    var selectedDateTime by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    val modelProject by viewModel.modelProject
    val registerText by viewModel.registerText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("Enter Post ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            id.toIntOrNull()?.let { viewModel.getData(it) }
        }) {
            Text("Get Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            modelProject?.let {
                val file = generatePdf(context, it, "report.pdf")
                Toast.makeText(context, "PDF saved to ${file.absolutePath}", Toast.LENGTH_LONG)
                    .show()
                openPdf(context, file)
            }
        }) {
            Text("Download PDF")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selected Date and Time: $selectedDateTime")

        Button(onClick = {
            showDateTimePickerDialog(context) { dateTime ->
                selectedDateTime = dateTime
                sendNotification(context, dateTime)
            }
        }) {
            Text("Pick Date and Time")
        }

        Text(text = registerText)

        modelProject?.let {
            Column {
                Text("ID: ${it.id}")
                Text("Title: ${it.title}")
                Text("Body: ${it.body}")
                Text("User ID: ${it.userId}")
            }
        }
    }
}

fun showDateTimePickerDialog(context: Context, onDateTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    // Show DatePickerDialog
    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        // Show TimePickerDialog
        TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            val selectedDateTime =
                "$selectedYear-${selectedMonth + 1}-$selectedDay $selectedHour:$selectedMinute"
            onDateTimeSelected(selectedDateTime)
        }, hour, minute, true).show()
    }, year, month, day).show()
}

fun sendNotification(context: Context, dateTime: String) {
    val channelId = "your_channel_id"
    val notificationId = 1

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    val intent = Intent(context, SendData::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Reminder")
        .setContentText("You have a scheduled event on $dateTime")
        .setSmallIcon(R.drawable.back) // Replace with your icon
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(notificationId, notification)
}


@Composable
fun tamrini() {

    var number by remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(15.dp)
    ) {
        Button(onClick = { number++ }) {
            Text(text = "add number = ${number}")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { number-- }) {
            Text(text = "cam number = ${number}")
        }
    }
}