package com.example.localapi.data.dto

import com.example.localapi.domain.model.Address
import com.example.localapi.domain.model.Location
import com.example.localapi.domain.model.MapView
import com.example.localapi.domain.model.Position

//fun LocationDto.toDomain(): Location {
//    return Location(
//        id = id,
//        title = title,
//        address = address.toDomain(),
//        position = position.toDomain(),
//        access = access.map { it.toDomain() },
//        mapView = mapView.toDomain()
//    )
//}

fun LocationDto.toDomain(): Location {
    return Location(
        id = id,
        title = title,
        address = address?.toDomain(),
        position = position?.toDomain(),
        access = access?.map { it: PositionDto -> it.toDomain() } ?: emptyList(),
        mapView = mapView?.toDomain()
    )
}


fun AddressDto.toDomain(): Address {
    return Address(
        label = label,
        countryCode = countryCode,
        countryName = countryName,
        stateCode = stateCode,
        state = state,
        county = county,
        city = city,
        district = district,
        street = street,
        postalCode = postalCode,
        houseNumber = houseNumber
    )
}

fun PositionDto.toDomain(): Position {
    return Position(
        lat = lat,
        lng = lng
    )
}

fun MapViewDto.toDomain(): MapView {
    return MapView(
        west = west,
        south = south,
        east = east,
        north = north
    )
}