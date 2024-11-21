package com.example.jetpackshop.ShowMap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.*

class ShowMapNew : ComponentActivity() {
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
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var destination by remember { mutableStateOf<LatLng?>(null) }
    var estimatedPrice by remember { mutableStateOf<Double?>(null) }
    var isPriceCalculated by remember { mutableStateOf(false) }

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
                getUserLocation(context) { location ->
                    currentLocation = location
                }
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

        Column(modifier = Modifier.fillMaxSize()) {
            // نمایش قیمت تخمینی در صورت محاسبه
            if (isPriceCalculated && estimatedPrice != null) {
                Text(text = "Estimated Price: ${estimatedPrice?.toInt()} تومان", modifier = Modifier.padding(16.dp))
            }

            // دکمه محاسبه قیمت
            Button(
                onClick = {
                    currentLocation?.let { origin ->
                        destination?.let { dest ->
                            // محاسبه قیمت
                            estimatedPrice = calculatePrice(origin, dest)
                            isPriceCalculated = true
                            // لاگ برای بررسی مقادیر
                            println("Price Calculated: $estimatedPrice")
                        } ?: run {
                            Toast.makeText(context, "Please select a destination", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Calculate Price")
            }

            if (isPriceCalculated && estimatedPrice != null) {
                Text(
                    text = "Estimated Price: ${estimatedPrice?.toInt()} تومان",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Text(
                    text = "Price not calculated yet.",
                    modifier = Modifier.padding(16.dp)
                )
            }

            // نمایش نقشه
            AndroidView(factory = {
                mapView.apply {
                    getMapAsync(OnMapReadyCallback { googleMap ->
                        // تنظیمات اولیه نقشه
                        val defaultLocation = LatLng(35.6892, 51.3890)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(defaultLocation)
                                .title("Tehran")
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

                        // مدیریت انتخاب مقصد
                        googleMap.setOnMapClickListener { latLng ->
                            destination = latLng
                            googleMap.clear()  // پاک کردن نشانگرهای قبلی
                            googleMap.addMarker(MarkerOptions().position(latLng).title("Destination"))
                        }
                    })
                }
            }, modifier = Modifier.fillMaxSize())
        }
    } else {
        // نمایش پیام در صورت عدم صدور مجوز
        Text(text = "Location permission is required to display the map.")
    }
}

// فرمول هارورد برای محاسبه فاصله بین دو نقطه جغرافیایی
fun calculateDistance(from: LatLng, to: LatLng): Double {
    val radius = 6371  // Earth's radius in km
    val latDiff = Math.toRadians(to.latitude - from.latitude)
    val lngDiff = Math.toRadians(to.longitude - from.longitude)

    val a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
            Math.cos(Math.toRadians(from.latitude)) * Math.cos(Math.toRadians(to.latitude)) *
            Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return radius * c  // Distance in km
}

// محاسبه قیمت
fun calculatePrice(origin: LatLng, destination: LatLng): Double {
    val distance = calculateDistance(origin, destination)
    val pricePerKm = 10.0  // Price per km (in Toman)
    return distance * pricePerKm
}

// گرفتن موقعیت کاربر
@SuppressLint("MissingPermission")
fun getUserLocation(context: Context, onLocationReceived: (LatLng?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                onLocationReceived(latLng)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowMapPreview() {
    MapScreen()
}
