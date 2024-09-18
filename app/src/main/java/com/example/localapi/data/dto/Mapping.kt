package com.example.localapi.data.dto

import com.example.localapi.domain.model.Location


fun LocationDto.toDomainModel(): Location {
    return Location(
        id = id,
        addressName = address.label,
        latitude = position.lat,
        longitude = position.lng,
        title = title
    )
}


fun ApiResponseDto.toDomainModel(): List<Location> {
    return items.map { it.toDomainModel() }
}


