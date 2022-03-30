package com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe

data class Location(
    val address: String,
    val country: String,
    val cross_street: String,
    val formatted_address: String,
    val locality: String,
    val neighborhood: List<String>,
    val postcode: String,
    val region: String
)