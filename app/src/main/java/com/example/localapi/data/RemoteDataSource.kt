package com.example.localapi.data

import LocalApiModel

class RemoteDataSource(private val apiService: HereApiService) {
    suspend fun searchAddress(query: String, limit: Int, apiKey: String): List<LocalApiModel.Item?>? {
        return apiService.searchAddress(query, limit, apiKey).items
    }
}