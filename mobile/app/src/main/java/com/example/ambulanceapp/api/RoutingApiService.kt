package com.example.ambulanceapp.api

import com.example.ambulanceapp.beans.NearestAmbulance
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RoutingApiService {
    @GET("/routings/nearest-ambulance")
    suspend fun getNearestAmbulance(
        @Query("patientLatitude") patientLatitude: Double,
        @Query("patientLongitude") patientLongitude: Double,
        @Query("specialty") specialty: String? = null,
    ): Response<NearestAmbulance>
}