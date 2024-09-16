package com.example.localapi.data.dto

import com.example.localapi.domain.model.Address
import com.example.localapi.domain.model.MapView
import com.example.localapi.domain.model.Position

data class LocationDto(
    val id: String?,
    val title: String?,
    val address: AddressDto?,
    val position: PositionDto?,
    val access: List<PositionDto>?,
    val mapView: MapViewDto?
)
