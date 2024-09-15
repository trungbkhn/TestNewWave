package com.example.localapi.di

import com.example.localapi.data.remote_data_source.RemoteDataSource
import com.example.localapi.data.repository.LocationRepositoryImpl
import com.example.localapi.data.services.ApiService
import com.example.localapi.data.services.RetrofitInstance
import com.example.localapi.domain.repository.LocationRepository
import com.example.localapi.domain.use_cases.SearchLocationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(remoteDataSource: RemoteDataSource): LocationRepository {
        return LocationRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideSearchLocationsUseCase(
        locationRepository: LocationRepository
    ): SearchLocationsUseCase {
        return SearchLocationsUseCase(locationRepository)
    }
}
