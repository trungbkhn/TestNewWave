package com.example.localapi.data.remote_data_source

import android.util.Log
import com.example.localapi.data.dto.LocationDto
import com.example.localapi.data.services.ApiService

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun searchAddress(query: String, limit: Int, apiKey: String): Result<List<LocationDto>> {
        return try {
            val response = apiService.searchLocations(query, limit, apiKey)
            Result.success(response.items)
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "API Error", e)
            Result.failure(e)
        }
    }
}

