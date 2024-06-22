package com.example.shoppinglistapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Locale

class LocationViewModel(
    private val shoppingRepository: ShoppingRepository =Graph.ShoppingRepository)
: ViewModel(){

    var shoppingItemName by mutableStateOf("")
    var shoppingItemQuantity by mutableStateOf("")


    fun ShoppingnNameChange(newString:String){
        shoppingItemName = newString
    }

    fun ShoppingnQtyChange(newString:String){
        shoppingItemQuantity = newString
    }


    lateinit var getAllShoppingItem: Flow<List<ShoppingItem>>

    init {
        viewModelScope.launch {
            getAllShoppingItem = shoppingRepository.getAllItems()
        }
    }

    fun addItem(item:ShoppingItem){
        viewModelScope.launch(Dispatchers.IO) {
            shoppingRepository.addItem(item=item)
        }
    }

    fun getItemById(id:Long) : Flow<ShoppingItem> {

        return  shoppingRepository.getItemById(id)
    }

    fun updateItem(item:ShoppingItem){
        viewModelScope.launch(Dispatchers.IO) {
            shoppingRepository.updateItem(item=item)
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingRepository.deleteItem(item)
        }
    }



    private val _location = mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address : State<List<GeocodingResult>> = _address
    fun updateLocation(newLocation : LocationData){
       _location.value = newLocation

    }

    fun fetchAddress(latlng:String,context:Context,locationData: LocationData){

 val geocoder = Geocoder(context, Locale.getDefault())
 val addresses: List<Address> = geocoder.getFromLocation(
     locationData.latitude,
     locationData.longitude,
     1
 ) ?: emptyList()

 if (addresses.isNotEmpty()) {
     val address = addresses[0]
     val geocodingResult = GeocodingResult(
         formattedAddress = address.getAddressLine(0) ?: "",
         locality = address.locality,
         adminArea = address.adminArea,
         countryName = address.countryName,
         postalCode = address.postalCode,
         featureName = address.featureName
     )
     _address.value = listOf(geocodingResult)
 } else {
     // Handle the case where no address is found
     _address.value = emptyList()
 }
        /* try {
             viewModelScope.launch {
                 val result = RetrofitClient.create().getAddressFromCoordinates(
                     latlng,
                     "your api needed"
                 )
                 println(result)
                 _address.value = result.results  // result is of type geocodingResult and it contains list of geocoding result
                 // which gives the address that can take the first entry of that and display it on screen of the user.
             }
         }catch (e : Exception){
             Log.d("res1","${e.cause} ${e.message}")
         // we can right the logcat the cause and message and finds under the tag res1
         }*/
}

}