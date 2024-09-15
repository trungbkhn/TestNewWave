package com.example.localapi.domain.model

data class Location(
    val id: String?,
    val title: String?,
    val address: Address?,
    val position: Position?,
    val access: List<Position>?,
    val mapView: MapView?
)