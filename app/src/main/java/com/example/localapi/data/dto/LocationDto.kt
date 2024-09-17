package com.example.localapi.data.dto

data class LocationDto(
    val title: String,
    val id: String,
    val address: AddressDto,
    val position: PositionDto
)
