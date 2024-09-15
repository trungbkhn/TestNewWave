package com.example.localapi.domain.repository

import com.example.localapi.domain.model.Location

interface LocationRepository {
    suspend fun searchLocations(query: String, limit: Int = 10): Result<List<Location>>
}



