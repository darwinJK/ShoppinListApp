package com.example.shoppinglistapp

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApiService {

    @GET("maps/api/geocode/json")  // creating a portion of url + latitude and longitude and key
    suspend fun getAddressFromCoordinates(
        @Query("latlng") latlng :String,
        @Query("key") apiKey : String
    ):GeocodingRespose // when we call this, it will return the json code
}