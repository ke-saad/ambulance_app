package com.example.ambulanceapp.api

import com.example.ambulanceapp.beans.Hospital
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HospitalApiService {
    @GET("/hospitals")
    suspend fun getAllHospitals(): Response<List<Hospital>>

    @GET("/hospitals/{id}")
    suspend fun getHospitalById(@Path("id") id: Long): Response<Hospital>
}