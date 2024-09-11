package com.example.localapi.data

import LocalApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface HereApiService {
    @GET("v1/geocode")
    suspend fun searchAddress(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("apiKey") apiKey: String
    ): LocalApiModel
}
