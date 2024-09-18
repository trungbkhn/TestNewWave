package com.example.localapi.data.repository


import android.util.Log
import com.example.localapi.Constant
import com.example.localapi.data.dto.toDomainModel
import com.example.localapi.data.remote_data_source.RemoteDataSource
import com.example.localapi.domain.model.Location
import com.example.localapi.domain.repository.LocationRepository
import com.example.localapi.utils.Result


class LocationRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : LocationRepository {
    override suspend fun searchLocations(query: String, limit: Int): Result<List<Location>> {
        return when (val result = remoteDataSource.searchAddress(query, limit, Constant.API_KEY)) {
            is Result.Success -> {
                val locationDtos = result.data
                Result.Success(locationDtos.map { it.toDomainModel() })
            }

            is Result.Failure -> {
                Log.e("LocationRepositoryImpl", "Error fetching locations", result.exception)
                Result.Failure(result.exception)
            }
        }
    }

    override suspend fun searchLocationOfArea(
        query: String,
        limit: Int,
        position: String
    ): Result<List<Location>> {
        return when (val result =
            remoteDataSource.searchAddressOfArea(query, limit, Constant.API_KEY, position)) {
            is Result.Success -> {
                val locationDtos = result.data
                Result.Success(locationDtos.map { it.toDomainModel() })
            }

            is Result.Failure -> {
                Log.e("LocationRepositoryImpl", "Error fetching locations", result.exception)
                Result.Failure(result.exception)
            }
        }

    }
}









