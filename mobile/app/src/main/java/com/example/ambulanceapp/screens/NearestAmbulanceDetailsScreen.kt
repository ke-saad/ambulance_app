package com.example.ambulanceapp.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ambulanceapp.MainViewModel
import com.example.ambulanceapp.beans.NearestAmbulance
import com.example.ambulanceapp.state.UiState

@Composable
fun NearestAmbulanceDetailsScreen(
    ambulanceId: String,
    viewModel: MainViewModel,
    navController: NavController
) {
    val nearestAmbulanceState by viewModel.nearestAmbulance.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEE))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (nearestAmbulanceState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is UiState.Success -> {
                    val nearestAmbulance = (nearestAmbulanceState as UiState.Success<*>).data as NearestAmbulance?

                    if (nearestAmbulance != null && nearestAmbulance.id != null) {
                        Text(
                            "Ambulance Details",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFFD32F2F)),
                            modifier = Modifier.padding(bottom = 24.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AmbulanceDetailRow(
                                label = "ID",
                                value = nearestAmbulance.id.toString()
                            )
                            AmbulanceDetailRow(
                                label = "Registration No",
                                value = nearestAmbulance.registrationNumber.orEmpty()
                            )
                            AmbulanceDetailRow(
                                label = "Model",
                                value = nearestAmbulance.model ?: "N/A"
                            )
                            AmbulanceDetailRow(
                                label = "Distance",
                                value = "${String.format("%.2f", nearestAmbulance.distance)} Km"
                            )
                            AmbulanceDetailRow(
                                label = "Phone",
                                value = nearestAmbulance.phone ?: "N/A"
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            CallAmbulanceButton(nearestAmbulance.phone, navController)
                        }
                    } else {
                        Text(
                            text = "Ambulance not found",
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = (nearestAmbulanceState as UiState.Error).message,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                else -> {
                    Text(
                        text = "Error loading data",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun CallAmbulanceButton(phone: String?, navController: NavController) {
    Button(
        onClick = {
            if (phone != null) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                navController.context.startActivity(intent)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = "Call Ambulance",
                tint = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Call Ambulance", color = Color.White, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AmbulanceDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.weight(0.6f)
        )
    }
}
