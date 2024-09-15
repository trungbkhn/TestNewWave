package com.example.localapi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.localapi.data.services.ApiService
import com.example.localapi.data.repository.LocationRepositoryImpl
import com.example.localapi.domain.use_cases.SearchLocationsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewModelFactory : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val apiService = Retrofit.Builder()
//            .baseUrl("https://geocode.search.hereapi.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//
//        val repository = LocationRepositoryImpl(apiService)
//        val useCase = SearchLocationsUseCase(repository)
//
//        return SearchViewModel(useCase) as T
//    }
}
