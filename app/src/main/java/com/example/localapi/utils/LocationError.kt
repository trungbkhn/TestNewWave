package com.example.localapi.utils

sealed class LocationError : Exception() {
    data object NoResults : LocationError() {
        private fun readResolve(): Any = NoResults
    }

    data object NetworkError : LocationError() {
        private fun readResolve(): Any = NetworkError
    }

    data class ApiError(val code: Int) : LocationError()
}
