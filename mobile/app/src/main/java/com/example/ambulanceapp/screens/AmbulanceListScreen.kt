package com.example.ambulanceapp.screens

import android.content.Intent
import android.net.Uri
import com.example.ambulanceapp.beans.Ambulance
import com.example.ambulanceapp.MainViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ambulanceapp.state.UiState

@Composable
fun EmptyStateScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF388E3C)),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error: $message",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F)),
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text(text = "Retry", color = Color.White)
            }
        }
    }
}

@Composable
fun AmbulanceListScreen(viewModel: MainViewModel, navController: NavController) {
    val ambulancesState by viewModel.ambulances.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAmbulances()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE5E5))
    ) {
        when (ambulancesState) {
            is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is UiState.Success -> {
                val ambulances = (ambulancesState as UiState.Success).data
                if (ambulances.isEmpty()) {
                    EmptyStateScreen(message = "No ambulances available.")
                } else {
                    AmbulanceList(
                        ambulances = ambulances,
                        onItemSelected = { selectedAmbulance ->
                            Log.d("AmbulanceList", "Selected ambulance: ${selectedAmbulance.id}")
                        },
                    )
                }
            }

            is UiState.Error -> ErrorScreen(message = (ambulancesState as UiState.Error).message) {
                viewModel.fetchAmbulances()
            }
        }
    }
}

@Composable
fun AmbulanceList(
    ambulances: List<Ambulance>,
    onItemSelected: (Ambulance) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(ambulances) { ambulance ->
            AmbulanceListItem(ambulance, onItemSelected)
        }
    }
}

@Composable
fun AmbulanceListItem(
    ambulance: Ambulance,
    onItemSelected: (Ambulance) -> Unit,
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onItemSelected(ambulance) }
            .shadow(8.dp, MaterialTheme.shapes.large),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Ambulance ID: ${ambulance.id}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Registration No: ${ambulance.registrationNumber}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Model: ${ambulance.model}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Status: ${ambulance.status}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Specialty: ${ambulance.specialty}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Phone: ${ambulance.phone}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (!ambulance.phone.isNullOrEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Call Ambulance",
                    tint = Color(0xFF355F2E),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${ambulance.phone}")
                            }
                            context.startActivity(intent)
                        }
                        .padding(start = 16.dp)
                        .size(36.dp)
                )
            }
        }
    }
}


