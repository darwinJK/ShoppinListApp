package com.example.shoppinglistapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


class LoUtilscation(val context : Context) {
//provides fundamental utilities & services like accessing resources,
// launching activities and more


    private val _fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    //allows  to get the latitude and longitude of user

    @SuppressLint("MissingPermission")
    // lint tool checks your android project source files for potential bugs and optimization improvement for correctness
    fun requestLocationupdates(viewModel: LocationViewModel) {
        val locationCallback = object : LocationCallback() {
            //request something wait for finalize that thing and it return something to you
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {  // gets the last location and unpack using let
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    viewModel.updateLocation(location)// fun updateLocation is gets replaced by the new location result
                }

            }
        }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        //high accuracy use high battery power, low uses low battery power, balanced balance the usage

        _fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )//SupressLint used fot ignore this error
//looper used to handle the threading and messages for processing location update,
//allows the provide to communicate with the apps main thread by provide a specific looper,
        // we can control which thread location updates will be delivered
    }

    fun haslocationPermisson(context: Context): Boolean {

        return ContextCompat.checkSelfPermission(//checks permission  return a boolean
            //checkSelfPermission returns int checks with permission granted int and return boolean
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED //checks the user grands permission
                // it return true
                &&
                ContextCompat.checkSelfPermission(//checks permission  return a boolean
                    //checkSelfPermission returns int checks with permission granted int and return boolean
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED //checks the user grands permission
    }


    /* fun reverseGeoCodeLocation(location: LocationData): String {
     val geocoder = Geocoder(context, Locale.getDefault()) // get current location to geocoder
     val coordinates = LatLng(location.latitude, location.longitude)
     // coordinate assigning to latLng that is latitude and longitude
     val addresses: MutableList<Address>? = //list can be change so mutable
         geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
     //getting the coordinates in one line in address
     return if (addresses?.isNotEmpty() == true) {
         addresses[0].getAddressLine(0) //getting addressLine
     } else {
         "Address not found"
     }
     }*/
}
