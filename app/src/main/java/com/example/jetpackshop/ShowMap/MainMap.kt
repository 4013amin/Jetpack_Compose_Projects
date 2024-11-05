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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    // درخواست مجوز دسترسی به موقعیت مکانی
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                hasLocationPermission = true
            }

            else -> {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // نمایش نقشه در صورتی که مجوز صادر شده باشد
    if (hasLocationPermission) {
        val mapView = remember { MapView(context) }

        // مدیریت چرخه‌ی حیات MapView
        DisposableEffect(Unit) {
            mapView.onCreate(null)
            mapView.onStart()
            mapView.onResume()

            onDispose {
                mapView.onPause()
                mapView.onStop()
                mapView.onDestroy()
            }
        }

        AndroidView(factory = {
            mapView.apply {
                getMapAsync(OnMapReadyCallback { googleMap ->
                    // تنظیمات اولیه نقشه و قرار دادن نشانگر در تهران
                    val tehran = LatLng(35.6892, 51.3890)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tehran, 10f))
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(tehran)
                            .title("Tehran")
                            .snippet("The capital of Iran")
                    )
                    googleMap.uiSettings.isZoomControlsEnabled = true

                    // فعال‌سازی موقعیت مکانی اگر مجوز صادر شده باشد
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        googleMap.isMyLocationEnabled = true
                    }
                })
            }
        }, modifier = Modifier.fillMaxSize())
    } else {
        // نمایش پیام در صورت عدم صدور مجوز
        Text(text = "Location permission is required to display the map.")
    }
}


@Preview(showBackground = true)
@Composable
fun ShowMapPreview() {
    MapScreen()
}
