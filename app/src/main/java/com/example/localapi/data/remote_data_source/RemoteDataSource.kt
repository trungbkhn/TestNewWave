package com.example.localapi.data.remote_data_source


import com.example.localapi.data.dto.LocationDto
import com.example.localapi.data.services.ApiService
import com.example.localapi.utils.LocationError
import com.example.localapi.utils.Result
import retrofit2.HttpException
import java.io.IOException

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun searchAddress(
        query: String,
        limit: Int,
        apiKey: String
    ): Result<List<LocationDto>> {
        if (limit == 0) {
            return Result.Success(emptyList())
        }
        return try {
            val response = apiService.searchLocations(query, limit, apiKey)
            val items = response.items

            if (items.isNullOrEmpty()) {
                Result.Failure(LocationError.NoResults)
            } else {
                Result.Success(items)
            }
        } catch (e: IOException) {
            Result.Failure(LocationError.NetworkError)
        } catch (e: HttpException) {
            Result.Failure(LocationError.ApiError(e.code()))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun searchAddressOfArea(
        query: String,
        limit: Int,
        apiKey: String,
        position: String
    ): Result<List<LocationDto>> {
        if (limit == 0) {
            return Result.Success(emptyList())
        }
        return try {
            val response = apiService.searchLocationOfArea(query, limit, position,apiKey)
            val items = response.items

            if (items.isNullOrEmpty()) {
                Result.Failure(LocationError.NoResults)
            } else {
                Result.Success(items)
            }

        } catch (e: IOException) {
            Result.Failure(LocationError.NetworkError)
        } catch (e: HttpException) {
            Result.Failure(LocationError.ApiError(e.code()))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}




