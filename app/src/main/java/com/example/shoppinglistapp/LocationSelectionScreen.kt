package com.example.shoppinglistapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale

@Composable
fun locationSelectionScreen(
    location:LocationData,
    onLocationSelected:(LocationData)->Unit){

    val userLocation = remember { mutableStateOf(LatLng(location.latitude,location.longitude)) } // users location where the phone is

    var cameraPositionState = rememberCameraPositionState{ //zoom to the area where the location of the phone
        // gmap location ,created to configure to initial state

        position = CameraPosition.fromLatLngZoom(userLocation.value,10f) //zoom is the default size of the google map.
    }


    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            cameraPositionState=cameraPositionState,
            onMapClick = {
                userLocation.value=it   // location which is clicked by the user has been set as the new location
                println("LocationlatitudeMain:   ${userLocation.value.latitude}  LocationlongitudeMain: ${userLocation.value.longitude}")
            }
        ) {
           Marker(state = MarkerState(position = userLocation.value))
        // the red marker which indicates the current location in  assign to the position of userLocation

        }
        var newLocation : LocationData
       // data class is initializes to new variable.
        Button(onClick = {
            newLocation = LocationData(userLocation.value.latitude,userLocation.value.longitude)
            //over writing the location of user Location to the data class and initializes to new variable.
            onLocationSelected(newLocation) //event which is called in fun. parameter

        }) {
            Text( "Set Location")
        }
    }


}

