package com.example.localapi.domain.use_cases

import com.example.localapi.domain.model.Location
import com.example.localapi.domain.repository.LocationRepository
import com.example.localapi.utils.Result

class SearchLocationsUseCase(private val locationRepository: LocationRepository) {
//    suspend operator fun invoke(query: String): Result<List<Location>> {
//        return locationRepository.searchLocations(query)
//    }
    suspend operator fun invoke(query: String, position: String): Result<List<Location>> =
        locationRepository.searchLocationOfArea(query, position = position)
}


