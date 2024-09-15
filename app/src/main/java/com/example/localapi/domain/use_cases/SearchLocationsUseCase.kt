package com.example.localapi.domain.use_cases

import android.util.Log
import com.example.localapi.domain.model.Location
import com.example.localapi.domain.repository.LocationRepository

//class SearchLocationsUseCase(private val locationRepository: LocationRepository) {
//    suspend operator fun invoke(query: String): Result<List<Location>> {
//        return try {
//            val locations = locationRepository.searchLocations(query)
//            Log.e("LocationUseCase", "isSuccess")
//            Result.success(locations)
//        } catch (e: Exception) {
//            Log.e("LocationUseCase", "fail")
//            Result.failure(e)
//        }
//    }
//}

class SearchLocationsUseCase(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(query: String): Result<List<Location>> {
        return locationRepository.searchLocations(query)
    }
}


