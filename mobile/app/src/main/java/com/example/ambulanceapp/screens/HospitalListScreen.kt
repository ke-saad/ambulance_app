package com.example.ambulanceapp.screens

import android.content.Intent
import android.net.Uri
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
import com.example.ambulanceapp.MainViewModel
import com.example.ambulanceapp.beans.Hospital
import com.example.ambulanceapp.state.UiState
import com.example.ambulanceapp.ui.EmptyStateScreen
import com.example.ambulanceapp.ui.ErrorScreen

@Composable
fun HospitalListScreen(viewModel: MainViewModel, navController: NavController) {
    val hospitalsState by viewModel.hospitals.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchHospitals()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE5E5))
    ) {
        when (hospitalsState) {
            is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is UiState.Success -> {
                val hospitals = (hospitalsState as UiState.Success).data
                if (hospitals.isEmpty()) {
                    EmptyStateScreen(message = "No hospitals available.")
                } else {
                    HospitalList(
                        hospitals = hospitals,
                        onItemSelected = { selectedHospital ->
                            Log.d("HospitalList", "Selected hospital: ${selectedHospital.id}")
                        },
                    )
                }
            }

            is UiState.Error -> ErrorScreen(message = (hospitalsState as UiState.Error).message) {
                viewModel.fetchHospitals()
            }
        }
    }
}

@Composable
fun HospitalList(
    hospitals: List<Hospital>,
    onItemSelected: (Hospital) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(hospitals) { hospital ->
            HospitalListItem(hospital, onItemSelected)
        }
    }
}

@Composable
fun HospitalListItem(
    hospital: Hospital,
    onItemSelected: (Hospital) -> Unit,
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onItemSelected(hospital) }
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
                    text = "Hospital ID: ${hospital.id}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Name: ${hospital.name}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Address: ${hospital.address}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Phone: ${hospital.phone}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Specialties: ${hospital.specialties?.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (!hospital.phone.isNullOrEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Call Hospital",
                    tint = Color(0xFF355F2E),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${hospital.phone}")
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
