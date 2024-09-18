package com.example.localapi.presentation.viewmodel

import android.util.Log
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

    private var searchJob: Job? = null

    fun searchLocation(query: String) {
        _searchKeyword.value = query
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(1000)  // Debounce 1 giây
            _loading.value = true
            val result = searchLocationsUseCase(query)
            // Sử dụng Result custom để xử lý kết quả
            when (result) {
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
}



