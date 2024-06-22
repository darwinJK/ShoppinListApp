package com.example.shoppinglistapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  navigation()
                }
            }
        }
    }
}
@Composable
fun navigation(){
    val navController = rememberNavController()
    val viewModel:LocationViewModel = viewModel()
    val context = LocalContext.current
    val loUtilscation= LoUtilscation(context)






    NavHost(navController, startDestination = "shoppingListScreen"){
        composable("shoppingListScreen"){
            ShoppingList(
                id=0L,
                loUtilscation = loUtilscation,
                viewModel = viewModel ,
                navController = navController,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formattedAddress?:" no address"



                //no matter how many items, need only one item if no item gets null
            )
        }
        dialog("locationScreen"){backstack->
            viewModel.location.value?.let { it1->
                locationSelectionScreen(location = it1, onLocationSelected = {locationData->
                    println("locationmainlat : ${locationData.latitude} locationmainlng : ${locationData.longitude}")
                  viewModel.fetchAddress("${locationData.latitude}, ${locationData.longitude}",context,
                      LocationData(locationData.latitude,locationData.longitude)
                  ) //it is location data.

                    navController.popBackStack()
                // attempt to pop the controller backstack, analogue to when the user presses the systems back button
                })
            }

        }
    }
}


