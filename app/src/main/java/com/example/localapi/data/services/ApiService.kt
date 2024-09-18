package com.example.localapi.data.services



import com.example.localapi.data.dto.ApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/geocode")
    suspend fun searchLocations(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("apiKey") apiKey: String
    ): ApiResponseDto

    @GET("v1/discover")
    suspend fun searchLocationOfArea(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("in") position: String,
        @Query("apiKey") apiKey: String
    ): ApiResponseDto
}
