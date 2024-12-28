package com.example.ambulanceapp.api

import com.example.ambulanceapp.beans.Ambulance
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AmbulanceApiService {
    @GET("/ambulances")
    suspend fun getAllAmbulances(): Response<List<Ambulance>>

    @GET("/ambulances/{id}")
    suspend fun getAmbulanceById(@Path("id") id: Long): Response<Ambulance>
}