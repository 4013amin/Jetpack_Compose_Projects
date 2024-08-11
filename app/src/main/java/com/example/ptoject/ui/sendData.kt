package com.example.ptoject.ui

import NotificationReceiver
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.ptoject.data.ViewModles.ViewModelsProject
import generatePdf
import openPdf
import java.io.File
import java.util.Calendar

class SendData : androidx.activity.ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
                val viewModel: ViewModelsProject = viewModel()
                GetData(viewModel, this)

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GetData(viewModel: ViewModelsProject, context: Context) {
    var id by remember { mutableStateOf("") }
    val modelProject by viewModel.modelProject
    val registerText by viewModel.registerText

    // States for date and time
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var datePickerDialog by remember { mutableStateOf<AlertDialog?>(null) }
    var timePickerDialog by remember { mutableStateOf<AlertDialog?>(null) }

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

        // Date and Time Picker Buttons
        Button(onClick = {
            // Show Date Picker Dialog
            val calendar = Calendar.getInstance()
            datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = "$year-${month + 1}-$dayOfMonth"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog?.show()
        }) {
            Text("Select Date")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // Show Time Picker Dialog
            val calendar = Calendar.getInstance()
            timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    selectedTime = "${hourOfDay}:${minute}"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog?.show()
        }) {
            Text("Select Time")
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

        Button(onClick = {
            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                scheduleNotification(context, selectedDate, selectedTime)
            } else {
                Toast.makeText(context, "Please select both date and time.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Schedule Notification")
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

@SuppressLint("ServiceCast")
@RequiresApi(Build.VERSION_CODES.O)
fun scheduleNotification(context: Context, date: String, time: String) {
    val calendar = Calendar.getInstance().apply {
        val (year, month, day) = date.split("-").map { it.toInt() }
        val (hour, minute) = time.split(":").map { it.toInt() }
        set(year, month - 1, day, hour, minute)
    }

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationChannelId = "your_channel_id"
    val channel = NotificationChannel(notificationChannelId, "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
        description = "Notification Channel Description"
    }
    notificationManager.createNotificationChannel(channel)

    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("notification_id", 1)
        putExtra("notification_title", "Scheduled Notification")
        putExtra("notification_message", "This is a scheduled notification.")
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
}


