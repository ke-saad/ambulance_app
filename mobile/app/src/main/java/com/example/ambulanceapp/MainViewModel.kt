package com.example.ambulanceapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ambulanceapp.api.AmbulanceApiService
import com.example.ambulanceapp.api.HospitalApiService
import com.example.ambulanceapp.api.RoutingApiService
import com.example.ambulanceapp.beans.Ambulance
import com.example.ambulanceapp.beans.Hospital
import com.example.ambulanceapp.beans.NearestAmbulance
import com.example.ambulanceapp.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainViewModel : ViewModel() {

    private val _ambulances = MutableStateFlow<UiState<List<Ambulance>>>(UiState.Loading)
    val ambulances: StateFlow<UiState<List<Ambulance>>> = _ambulances

    private val _hospitals = MutableStateFlow<UiState<List<Hospital>>>(UiState.Loading)
    val hospitals: StateFlow<UiState<List<Hospital>>> = _hospitals

    private val _nearestAmbulance = MutableStateFlow<UiState<NearestAmbulance>>(UiState.Loading)
    val nearestAmbulance: StateFlow<UiState<NearestAmbulance>> = _nearestAmbulance

    var contentType = "application/json"
    private var acceptType = "application/json"
    private lateinit var retrofit: Retrofit
    lateinit var ambulanceApiService: AmbulanceApiService
    lateinit var hospitalApiService: HospitalApiService
    lateinit var routingApiService: RoutingApiService

    init {
        setupApiService(GsonConverterFactory.create())
    }

    private fun setupApiService(converterFactory: Converter.Factory) {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request: Request = chain.request()
                .newBuilder()
                .header("Content-Type", contentType)
                .header("Accept", acceptType)
                .build()
            chain.proceed(request)
        }.build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8082/")
            .client(client)
            .addConverterFactory(converterFactory)
            .build()

        ambulanceApiService = retrofit.create(AmbulanceApiService::class.java)
        hospitalApiService = retrofit.create(HospitalApiService::class.java)
        routingApiService = retrofit.create(RoutingApiService::class.java)
    }

    fun getAmbulanceById(id: Long): Ambulance? {
        val currentAmbulanceState = _ambulances.value
        return if (currentAmbulanceState is UiState.Success) {
            val ambulance = currentAmbulanceState.data.find { it.id == id }
            ambulance?.let {
                Ambulance(
                    id = it.id,
                    registrationNumber = it.registrationNumber,
                    model = it.model,
                    status = it.status,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    phone = it.phone
                )
            }
        } else {
            null
        }
    }

    fun fetchAmbulances() {
        viewModelScope.launch {
            _ambulances.value = UiState.Loading
            try {
                val response = ambulanceApiService.getAllAmbulances()
                if (response.isSuccessful) {
                    _ambulances.value = UiState.Success(response.body() ?: emptyList())
                } else {
                    _ambulances.value = UiState.Error("Failed to fetch ambulances")
                }

            } catch (e: Exception) {
                _ambulances.value = UiState.Error("Error: ${e.message}")
            }
        }
    }

    fun fetchHospitals() {
        viewModelScope.launch {
            _hospitals.value = UiState.Loading
            try {
                val response = hospitalApiService.getAllHospitals()
                if (response.isSuccessful) {
                    _hospitals.value = UiState.Success(response.body() ?: emptyList())
                } else {
                    _hospitals.value = UiState.Error("Failed to fetch hospitals")
                }

            } catch (e: Exception) {
                _hospitals.value = UiState.Error("Error: ${e.message}")
            }
        }
    }

    fun findNearestAmbulance(latitude: Double, longitude: Double, specialty: String?) {
        viewModelScope.launch {
            _nearestAmbulance.value = UiState.Loading
            try {
                val response = routingApiService.getNearestAmbulance(latitude, longitude, specialty)
                if (response.isSuccessful) {
                    _nearestAmbulance.value = UiState.Success(response.body() ?: NearestAmbulance())
                } else {
                    _nearestAmbulance.value = UiState.Error("Failed to find nearest ambulance")
                }
            } catch (e: Exception) {
                _nearestAmbulance.value = UiState.Error("Error: ${e.message}")
            }
        }
    }
}