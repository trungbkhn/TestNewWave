package com.example.localapi.data.repository

import android.util.Log
import com.example.localapi.Constant
import com.example.localapi.data.dto.toDomain
import com.example.localapi.data.remote_data_source.RemoteDataSource
import com.example.localapi.domain.model.Location
import com.example.localapi.domain.repository.LocationRepository
import com.example.localapi.utils.LocationError
import retrofit2.HttpException
import java.io.IOException

//class LocationRepositoryImpl(
//    private val remoteDataSource: RemoteDataSource
//) : LocationRepository {
//
//    override suspend fun searchLocations(query: String): List<Location> {
//        val result = remoteDataSource.searchAddress(query, limit = 10, apiKey = Constant.API_KEY)
//
//        return if (result.isSuccess) {
//            Log.e("LocationRepositoryImpl", "isSuccess")
//            val locationDtos = result.getOrNull()
//            if (!locationDtos.isNullOrEmpty()) {
//                Log.e("LocationRepositoryImpl", "location not empty")
//                Log.e("LocationRepositoryImpl", "location: "+ locationDtos.get(0))
//                locationDtos.map { it.toDomain() }
//            } else {
//                Log.e("LocationRepositoryImpl", "location empty")
//                emptyList()
//
//            }
//        } else {
//            val exception = result.exceptionOrNull()
//            Log.e("LocationRepository", "Error fetching locations", exception)
//            when (exception) {
//                is IOException -> throw LocationError.NetworkError
//                is HttpException -> throw LocationError.ApiError(exception.code())
//                else -> throw exception ?: Exception("Unknown error")
//            }
//        }
//    }
//}

class LocationRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : LocationRepository {

    override suspend fun searchLocations(query: String, limit: Int): Result<List<Location>> {
        return try {
            val result = remoteDataSource.searchAddress(query, limit = 10, apiKey = Constant.API_KEY)
            val locationDtos = result.getOrNull()

            if (!locationDtos.isNullOrEmpty()) {
                Result.success(locationDtos.map { it.toDomain() })
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.e("LocationRepositoryImpl", "Error fetching locations", e)
            when (e) {
                is IOException -> Result.failure(LocationError.NetworkError)
                is HttpException -> Result.failure(LocationError.ApiError(e.code()))
                else -> Result.failure(e)
            }
        }
    }
}





