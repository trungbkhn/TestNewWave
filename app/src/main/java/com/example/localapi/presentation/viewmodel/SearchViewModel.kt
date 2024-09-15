package com.example.localapi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localapi.domain.model.Location
import com.example.localapi.domain.use_cases.SearchLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


//@HiltViewModel
//class SearchViewModel @Inject constructor(
//    private val searchLocationsUseCase: SearchLocationsUseCase
//) : ViewModel() {
//
//    // LiveData để chứa danh sách kết quả tìm kiếm
//    private val _searchResults = MutableLiveData<List<Location>>()
//    val searchResults: LiveData<List<Location>> get() = _searchResults
//
//    private val _searchKeyword = MutableLiveData<String>()
//    val searchKeyword: LiveData<String> get() = _searchKeyword
//
//    // LiveData để quản lý trạng thái lỗi
//    private val _errorState = MutableLiveData<String?>()
//    val errorState: LiveData<String?> get() = _errorState
//
//    // LiveData để quản lý trạng thái tải
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> get() = _loading
//
//    // Xử lý debounce bằng Coroutine
//    private var searchJob: Job? = null
//
//    fun searchLocation(query: String) {
//        _searchKeyword.value = query
//        // Cancel job trước đó nếu người dùng tiếp tục nhập
//        searchJob?.cancel()
//
//        // Đảm bảo debounce 1 giây trước khi thực hiện tìm kiếm
//        searchJob = viewModelScope.launch {
//            delay(1000)  // Debounce 1 giây
//
//            _loading.value = true  // Hiển thị trạng thái tải
//            val result = searchLocationsUseCase(query)  // Gọi Use Case
//
//            result.onSuccess { locations ->
//                Log.e("LocationViewModel", "location not empty")
//                _searchResults.value = locations
//                _errorState.value = null  // Xóa trạng thái lỗi
//            }.onFailure { exception ->
//                Log.e("LocationViewModel", "location empty")
//                _searchResults.value = emptyList()  // Nếu lỗi, trả về danh sách rỗng
//                _errorState.value = exception.message  // Hiển thị thông báo lỗi
//            }
//            _loading.value = false  // Kết thúc trạng thái tải
//        }
//    }
//}


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchLocationsUseCase: SearchLocationsUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Location>>()
    val searchResults: LiveData<List<Location>> get() = _searchResults

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

            result.onSuccess { locations ->
                _searchResults.value = locations
                _errorState.value = null  // Xóa lỗi nếu có
                Log.e("LocationViewModel", "location not empty. location 1 is:"+locations.get(0))
            }.onFailure { exception ->
                Log.e("LocationViewModel", "location empty")
                _searchResults.value = emptyList()
                _errorState.value = exception.message  // Hiển thị thông báo lỗi
            }

            _loading.value = false
        }
    }
}

