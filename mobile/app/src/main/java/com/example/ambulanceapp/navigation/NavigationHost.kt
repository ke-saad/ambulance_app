package com.example.ambulanceapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ambulanceapp.HomeScreen
import com.example.ambulanceapp.MainViewModel
import com.example.ambulanceapp.screens.AmbulanceListScreen
import com.example.ambulanceapp.screens.HospitalListScreen
import com.example.ambulanceapp.screens.FindNearestAmbulanceScreen
import com.example.ambulanceapp.screens.NearestAmbulanceDetailsScreen


@Composable
fun NavigationHost(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, mainViewModel = viewModel) }
        composable("ambulances") {
            AmbulanceListScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("hospitals") {
            HospitalListScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("find_nearest_ambulance") {
            FindNearestAmbulanceScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("nearest_ambulance_details/{ambulanceId}") { backStackEntry ->
            val ambulanceId = backStackEntry.arguments?.getString("ambulanceId") ?: return@composable
            NearestAmbulanceDetailsScreen(ambulanceId = ambulanceId, viewModel = viewModel, navController = navController)
        }
    }
}