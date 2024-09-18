package com.example.localapi.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localapi.domain.model.Location
import com.example.localapi.domain.use_cases.SearchLocationsUseCase
import com.example.localapi.utils.LocationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchLocationsUseCase: SearchLocationsUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Location>?>()
    val searchResults: LiveData<List<Location>?> get() = _searchResults

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword: LiveData<String> get() = _searchKeyword

    private val _errorState = MutableLiveData<String?>()
    val errorState: LiveData<String?> get() = _errorState

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _position = MutableLiveData<String?>()
    val position: LiveData<String?> get() = _position


    private var searchJob: Job? = null

    fun updatePosition(position: String) {
        _position.value = position
    }

    fun searchLocationOfArea(query: String){
        val currentPosition = _position.value
        if (currentPosition == null) {
            _errorState.value = "User location is not available"
            return
        }
        _searchKeyword.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1000)
            _loading.value = true
            when (val result = searchLocationsUseCase(query,currentPosition)) {
                is com.example.localapi.utils.Result.Success -> {
                    val locations = result.data
                    _searchResults.value = locations
                    _errorState.value = null
                }

                is com.example.localapi.utils.Result.Failure -> {
                    when (val error = result.exception) {
                        is LocationError.NoResults -> {
                            _searchResults.value = emptyList()
                            _errorState.value = "No locations found"
                        }

                        is LocationError.NetworkError -> {
                            _searchResults.value = emptyList()
                            _errorState.value = "Network error, please try again later"
                        }

                        is LocationError.ApiError -> {
                            _searchResults.value = emptyList()
                            _errorState.value = "API error: ${error.code}"
                        }

                        else -> {
                            _searchResults.value = emptyList()
                            _errorState.value = error.message ?: "An unexpected error occurred"
                        }
                    }
                }
            }

            _loading.value = false
        }
    }

//    fun searchLocation(query: String) {
//        _searchKeyword.value = query
//        searchJob?.cancel()
//
//
//        searchJob = viewModelScope.launch {
//            delay(1000)  // Debounce 1 giây
//            _loading.value = true
//            when (val result = searchLocationsUseCase(query)) {
//                is com.example.localapi.utils.Result.Success -> {
//                    val locations = result.data
//                    _searchResults.value = locations
//                    _errorState.value = null
//                }
//
//                is com.example.localapi.utils.Result.Failure -> {
//                    when (val error = result.exception) {
//                        is LocationError.NoResults -> {
//                            _searchResults.value = emptyList()
//                            _errorState.value = "No locations found"
//                        }
//
//                        is LocationError.NetworkError -> {
//                            _searchResults.value = emptyList()
//                            _errorState.value = "Network error, please try again later"
//                        }
//
//                        is LocationError.ApiError -> {
//                            _searchResults.value = emptyList()
//                            _errorState.value = "API error: ${error.code}"
//                        }
//
//                        else -> {
//                            _searchResults.value = emptyList()
//                            _errorState.value = error.message ?: "An unexpected error occurred"
//                        }
//                    }
//                }
//            }
//
//            _loading.value = false
//        }
//    }
    private fun handleSearchFailure(error: LocationError) {
        _searchResults.value = emptyList()
        _errorState.value = when (error) {
            is LocationError.NoResults -> "No locations found"
            is LocationError.NetworkError -> "Network error, please try again later"
            is LocationError.ApiError -> "API error: ${error.code}"
            else -> error.message ?: "An unexpected error occurred"
        }
    }
}



