package com.example.localapi.data.dto

data class LocationDto(
    val id: String?,
    val title: String?,
    val address: AddressDto?,
    val position: PositionDto?,
    val access: List<PositionDto>?,
    val mapView: MapViewDto?
)
