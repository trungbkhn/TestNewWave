package com.example.localapi.data.dto

data class AddressDto(
    val label: String,
    val countryCode: String,
    val countryName: String,
    val stateCode: String,
    val state: String,
    val county: String,
    val city: String,
    val district: String,
    val street: String,
    val postalCode: String,
    val houseNumber: String
)