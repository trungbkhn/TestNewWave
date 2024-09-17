package com.example.localapi.domain.use_cases

import com.example.localapi.domain.model.Location
import com.example.localapi.domain.repository.LocationRepository
import com.example.localapi.utils.Result

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


