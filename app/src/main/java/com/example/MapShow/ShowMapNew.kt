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
import android.util.Log
import androidx.compose.ui.res.painterResource
import java.io.IOException

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
    var destinationLocation by remember { mutableStateOf<LatLng?>(null) }
    val mapView = remember { MapView(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

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
            AndroidView(factory = { mapView }, modifier = Modifier.weight(1f)) { map ->
                map.getMapAsync { googleMap ->
                    googleMap.setOnMapClickListener { latLng ->
                        destinationLocation = latLng
                        destinationLocation?.let { destination ->
                            currentLocation?.let { origin ->
                                // Fetch and draw route
                                fetchAndDrawRoute(
                                    context,
                                    googleMap,
                                    origin,
                                    destination
                                )
                            }
                        }
                    }

                    // Start live location updates
                    startUberLocationUpdates(context) { location ->
                        currentLocation = location
                        googleMap.clear() // Clear old markers and polylines

                        // Add marker at the current location
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(location)
                                .title("Your Location")
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getBitmapFromDrawable(context, R.drawable.baseline_person_24)
                                    )
                                )
                        )

                        // Move camera to the current location
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(location, 15f)
                        )
                    }
                }
            }
        }
    } else {
        Text(
            text = "Location permission is required to display the map.",
            modifier = Modifier.padding(16.dp)
        )
    }
}

fun fetchAndDrawRoute(
    context: Context,
    googleMap: com.google.android.gms.maps.GoogleMap,
    origin: LatLng,
    destination: LatLng
) {
    val apiKey = "AIzaSyCE10md9NLiGzDpcciCJlnoUYkQdIz0YHE"
    val url = "https://maps.googleapis.com/maps/api/directions/json?" +
            "origin=${origin.latitude},${origin.longitude}" +
            "&destination=${destination.latitude},${destination.longitude}" +
            "&key=$apiKey"

    val client = okhttp3.OkHttpClient()
    val request = okhttp3.Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            Toast.makeText(context, "Failed to fetch route: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                responseData?.let {
                    val polylinePoints = parsePolylinePoints(it)
                    googleMap.addPolyline(
                        com.google.android.gms.maps.model.PolylineOptions()
                            .addAll(polylinePoints)
                            .width(10f)
                            .color(android.graphics.Color.BLUE)
                    )
                }
            }
        }
    })
}

fun parsePolylinePoints(response: String): List<LatLng> {
    val jsonObject = org.json.JSONObject(response)
    val routes = jsonObject.getJSONArray("routes")
    if (routes.length() == 0) {
        Log.e("DirectionsAPI", "No routes found")
        return emptyList<LatLng>()
    }
    val overviewPolyline = routes.getJSONObject(0)
        .getJSONObject("overview_polyline")
        .getString("points")
    return decodePolyline(overviewPolyline)

}

fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dLat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dLng

        poly.add(LatLng(lat / 1E5, lng / 1E5))
    }

    return poly
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
