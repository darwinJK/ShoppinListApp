package com.example.shoppinglistapp

data class LocationData(
    var latitude : Double,
    var longitude : Double
)

data class GeocodingRespose(
    val results : List<GeocodingResult>,
    val status :String
)

data class GeocodingResult(
    val formattedAddress: String,


    val locality: String?,
    val adminArea: String?,
    val countryName: String?,
    val postalCode: String?,
    val featureName: String?
)