package com.example.ambulanceapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.ambulanceapp.MainViewModel
import com.example.ambulanceapp.beans.NearestAmbulance
import com.example.ambulanceapp.state.UiState
import com.example.ambulanceapp.ui.ErrorScreen
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Composable
fun FindNearestAmbulanceScreen(viewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var nearestAmbulance by remember { mutableStateOf<NearestAmbulance?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    var isGPSEnabled by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val nearestAmbulanceState by viewModel.nearestAmbulance.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val specialties = listOf("Cardiology", "Neurology", "Trauma", "Pediatrics", "General", "Oncology", "Respiratory")

    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                scope.launch {
                    getLocation(context) { newLat, newLon ->
                        latitude = newLat.toString()
                        longitude = newLon.toString()
                        isGPSEnabled = true
                    }
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                scope.launch {
                    getLocation(context) { newLat, newLon ->
                        latitude = newLat.toString()
                        longitude = newLon.toString()
                        isGPSEnabled = true
                    }
                }
            }
            else -> {
                scope.launch {
                    showSnackbar = true
                    snackbarHostState.showSnackbar("Location permission is required to use GPS")
                    isGPSEnabled = false
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEE)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Find Nearest Ambulance",
                style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFFD32F2F)))

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        scope.launch {
                            getLocation(context) { newLat, newLon ->
                                latitude = newLat.toString()
                                longitude = newLon.toString()
                                isGPSEnabled = true
                            }
                        }
                    } else {
                        locationPermissionRequest.launch(arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text(
                    text = "Use GPS Location",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (!isGPSEnabled) {
                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text(
                        text = "Open Location Settings",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(
                    text = "Open App Permissions",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Patient Latitude") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isGPSEnabled,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Patient Longitude") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isGPSEnabled,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = specialty,
                    onValueChange = { },
                    label = { Text("Specialty (Optional)") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.clickable { expanded = true }
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Red
                    )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    specialties.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                specialty = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (latitude.isEmpty() || longitude.isEmpty()) {
                        scope.launch {
                            showSnackbar = true
                            snackbarHostState.showSnackbar("Please enter latitude and longitude")
                        }
                        return@Button
                    }
                    val latitudeDouble = latitude.toDoubleOrNull()
                    val longitudeDouble = longitude.toDoubleOrNull()
                    if (latitudeDouble == null || longitudeDouble == null) {
                        scope.launch {
                            showSnackbar = true
                            snackbarHostState.showSnackbar("Invalid latitude or longitude")
                        }
                        return@Button
                    }

                    viewModel.findNearestAmbulance(
                        latitudeDouble,
                        longitudeDouble,
                        if (specialty.isNotEmpty()) specialty else null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
            ) {
                Text(
                    text = "Find Nearest Ambulance",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (nearestAmbulanceState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success<*> -> {
                    nearestAmbulance = (nearestAmbulanceState as UiState.Success).data as NearestAmbulance?
                    if (nearestAmbulance?.id != null) {
                        navController.navigate("nearest_ambulance_details/${nearestAmbulance?.id}")
                    } else {
                        Text(
                            text = "No ambulance found",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is UiState.Error -> ErrorScreen(
                    message = (nearestAmbulanceState as UiState.Error).message
                ) {
                    viewModel.findNearestAmbulance(
                        latitude.toDouble(),
                        longitude.toDouble(),
                        if (specialty.isNotEmpty()) specialty else null
                    )
                }
                else -> {}
            }

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

@SuppressLint("MissingPermission")
private suspend fun getLocation(context: Context, onLocationReceived: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    try {
        val location = fusedLocationClient.lastLocation.await()
        if (location != null) {
            onLocationReceived(location.latitude, location.longitude)
        } else {
            val task = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            val loc = task.await()
            if (loc != null) {
                onLocationReceived(loc.latitude, loc.longitude)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}