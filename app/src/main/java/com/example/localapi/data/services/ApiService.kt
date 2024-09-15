package com.example.localapi.data.services

import com.example.localapi.data.dto.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/geocode")
    suspend fun searchLocations(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("apiKey") apiKey: String
    ): ApiResponse
}
