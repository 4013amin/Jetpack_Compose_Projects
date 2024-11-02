package com.example.jetpackshop.ShowMap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.neshan.maps.MapView
import com.neshan.maps.model.LatLng
import com.neshan.maps.model.MarkerOptions

class MainMap : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen()
                }
            }
        }
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.entries.all { it.value }
    }

    LaunchedEffect(Unit) {
        val fineLocationPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationPermissionGranted) {
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            hasLocationPermission = true
        }
    }

    if (hasLocationPermission) {
        // MapView باید به صورت Stateful باشد
        val mapView = remember { MapView(context) }
        AndroidView(factory = { mapView }) { mapView ->
            // تنظیمات نقشه
            mapView.getMapAsync { googleMap ->
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(35.6892, 51.3890), 10f))
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(35.6892, 51.3890))
                        .title("Tehran")
                        .snippet("The capital of Iran")
                )
                googleMap.isMyLocationEnabled = true // نشان دادن مکان کاربر
            }
        }
    } else {
        // در صورتی که مجوز داده نشده است، می‌توانید پیام خطا یا UI دیگری نمایش دهید
    }
}

@Preview(showBackground = true)
@Composable
private fun showMap() {
    MapScreen()
}
