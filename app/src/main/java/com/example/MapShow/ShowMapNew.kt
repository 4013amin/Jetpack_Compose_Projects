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
import androidx.core.content.ContextCompat
import com.example.jetpackshop.R
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.compose.ui.res.painterResource

class ShowMapNew : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UberStyleMapScreen()
                }
            }
        }
    }
}

@Composable
fun UberStyleMapScreen() {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val mapView = remember { MapView(context) }

    // Permission launcher for location
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            hasLocationPermission = true
        } else {
            Toast.makeText(context, "Location permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    // Check for permission and request if not granted
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> hasLocationPermission = true

            else -> permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (hasLocationPermission) {
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
            // Display Map
            AndroidView(factory = { mapView }, modifier = Modifier.weight(1f)) { map ->
                map.getMapAsync { googleMap ->
                    // Start live location updates
                    startUberLocationUpdates(context) { location ->
                        currentLocation = location
                        googleMap.clear() // Clear old markers

                        // Add marker at the current location with custom icon
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(location)
                                .title("Your Location")
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getBitmapFromDrawable(
                                            context,
                                            R.drawable.baseline_person_24
                                        )
                                    )
                                ) // Replace with your drawable
                        )

                        // Move camera to the current location
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(location, 15f)
                        )
                    }
                }
            }

            // Button to Center Map on Current Location
            Button(
                onClick = {
                    currentLocation?.let { location ->
                        mapView.getMapAsync { googleMap ->
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(location, 15f)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Center Map on Current Location")
            }
        }
    } else {
        Text(
            text = "Location permission is required to display the map.",
            modifier = Modifier.padding(16.dp)
        )
    }
}


@SuppressLint("MissingPermission")
fun startUberLocationUpdates(
    context: Context,
    onLocationUpdated: (LatLng) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        2000L // Update every 2 seconds
    ).build()

    val locationCallback = object : com.google.android.gms.location.LocationCallback() {
        override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
            locationResult.locations.lastOrNull()?.let { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                onLocationUpdated(latLng)
            }
        }
    }

    // Start location updates
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        context.mainLooper
    )
}

fun getBitmapFromDrawable(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
        ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun calculateDistance(from: LatLng, to: LatLng): Double {
    val radius = 6371.0 // Earth's radius in km
    val latDiff = Math.toRadians(to.latitude - from.latitude)
    val lngDiff = Math.toRadians(to.longitude - from.longitude)

    val a = sin(latDiff / 2).pow(2) +
            cos(Math.toRadians(from.latitude)) * cos(Math.toRadians(to.latitude)) *
            sin(lngDiff / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return radius * c
}

fun calculatePrice(origin: LatLng, destination: LatLng): Double {
    val distance = calculateDistance(origin, destination)
    val pricePerKm = 10.0 // Price per km (in Toman)
    return distance * pricePerKm
}

@Preview(showBackground = true)
@Composable
fun ShowMapPreview() {
    JetPackShopTheme {
        UberStyleMapScreen()
    }
}
