package com.example.localapi.utils

sealed class LocationError : Exception() {
    data object NoResults : LocationError()
    data object NetworkError : LocationError()
    data class ApiError(val code: Int) : LocationError()
}
