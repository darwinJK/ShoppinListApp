@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shoppinglistapp


import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale
import androidx.compose.material3.AlertDialog as AlertDialog1


@Entity(tableName = "Shopping-table")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    @ColumnInfo(name = "wish-name")
    var name: String,
    @ColumnInfo(name = "wish-qty")
    var quantity: Int,
    var isEditing: Boolean = false,
    @ColumnInfo(name ="wish-address")
    var address: String = ""
)



@Composable
fun ShoppingList(
    id:Long,
    loUtilscation: LoUtilscation,
    viewModel: LocationViewModel,
    navController: NavController,
    context: Context, //neccessory for permission
    address : String,


){

    //sItem contains of list in shoppingItem(data class)

    var showDialog by remember { mutableStateOf(false) }





    val requestPermissionLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true
                &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true){

                loUtilscation.requestLocationupdates(viewModel=viewModel)


            }else{
                //Ask for permission
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    // telling user y we need this permission.
                    context as MainActivity, // do the ratione screen on mainactivity
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    // telling user y we need this permission.
                    context as MainActivity, // do the ratione screen on mainactivity
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                //rationerequired is boolean
                if(rationaleRequired) {
                    Toast.makeText(
                        context,//popup that displayed at the bottom of the screen is toast
                        "Location permission is required for this feature to work",
                        Toast.LENGTH_LONG
                    ).show() // long we want to display is length long
                }else{
                    Toast.makeText(context,//popup that displayed at the bottom of the screen is toast
                        "Location permission is required, please enable  in android settings",
                        Toast.LENGTH_LONG).show()
                }
            }
        })



    // #first column contains button and a lazy column
    Column(modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center) {

        //button to add item
        Button(onClick = { showDialog=true
                         viewModel.shoppingItemName=""
                         viewModel.shoppingItemQuantity=""},
            modifier= Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item")

        }
        val shoppingList by viewModel.getAllShoppingItem.collectAsState(initial = listOf())

        //lazycolumn allows to display long list without overwhlemed the system.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            //items used for multiple elements we have to

            items(shoppingList){
                item->

                //the editing item is true on the current item.
                if(item.isEditing){
                    //checking for the item is edited is true
                    shoppingItemEditor(item = item, onEditComplete ={
                        editedName, editedQty ->
                        //map{} is used for iterate through the list.
                        //copy() : coping the content without affecting the orgnial list

                        val editedItem = item.copy(name = editedName, quantity = editedQty, isEditing = false)
                        viewModel.updateItem(editedItem)
                    //    editedItem?.let {
                      //      it.name=editedName
                        //    it.quantity=editedQty
                          //  it.address = address
                         //}
                    } )
                }
                else{
                    shoppingListItem(item = item,
                        //finding out which item we are editing and end changing is " isediting "boolean to true
                        //function on editing the item from the list.
                        onEditClick = {
                            val updatedItem = item.copy(isEditing = true)
                            viewModel.updateItem(updatedItem)
                       // sItems=sItems.map { it.copy(isEditing = it.id==item.id) }
                                      },
                        //function of deletiing item
                        OnDeleteClick = {
                        viewModel.deleteItem(item)}


                    )
                }
            }
        }
    }
        //when we use the button 'add item' this will executes.
    if(showDialog){
        //alertdialogue is used to show the list of items.
        //its enables only on experimental  through api3(check the above line of composables)
        AlertDialog1(onDismissRequest = { showDialog=false },

            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                //button for add the entered item.
                                Button(onClick = {
                                    //check for any blank column
                                    if(viewModel.shoppingItemName.isNotEmpty()){
                                        showDialog=false
                                        viewModel.addItem(ShoppingItem(
                                            id = id,
                                            name = viewModel.shoppingItemName,
                                            quantity = viewModel.shoppingItemQuantity.toIntOrNull()?:1,
                                            address=address
                                        ))
                                    }

                                }) {
                                    Text(text = "Add")
                                }
                              //  Spacer(modifier = Modifier.width(20.dp))

                                //button to cancelling add item.
                                Button(onClick = { showDialog=false }) {
                                    Text(text = "Cancel")
                                }
                            }
            },
            title = { Text( "Add Shopping Item")},

            text={

                //column for the user to add item and its quantity.
                Column {
                    //the given text that we provide in side the textfield is declared to 'it'
                    OutlinedTextField(value = viewModel.shoppingItemName, onValueChange ={
                        viewModel.shoppingItemName=it },label={ Text(text = "Item Name")},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))


                    OutlinedTextField(value = viewModel.shoppingItemQuantity, onValueChange ={

                            viewModel.shoppingItemQuantity=it},
                        label={ Text(text = "Item qty")},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))
                    
                    Button(onClick = { 
                        if(loUtilscation.haslocationPermisson(context)){
                            loUtilscation.requestLocationupdates(viewModel)
                            navController.navigate("locationScreen"){
                                this.launchSingleTop //boolean takes whether the navigation ation should launch a singleTop
                                //will atmost one copy of of a given destination on the top of the backstack.
                            }
                        }else{
                            requestPermissionLauncher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        }
                    }) {
                        Text(text = "Address")
                    }

                }

            })

    }
}

@Composable
//The function for the working of edit icon.
fun shoppingItemEditor(item : ShoppingItem , onEditComplete : (String, Int) -> Unit){
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQty by remember{ mutableStateOf(item.quantity.toString()) }

    //creating row at the position of the edited item
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly)
    {

        //column inside the row to edit the item name and quantity.
        Column {
            //field for editing item name
            BasicTextField(value = editedName.trim(), onValueChange = {editedName =it},
                
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
            //field for editing quantity
            BasicTextField(value = editedQty.trim(), onValueChange = {
                    editedQty=it
                },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }
        //button outside the column used to save the edited details.
        Button(
            onClick = {

                onEditComplete(editedName, editedQty.toIntOrNull()?:1)
            }
        ){
            Text("Save")
        }
    }
}

@Composable
//function used to add the list of items in the interface.
fun shoppingListItem(item: ShoppingItem,
                     onEditClick : () -> Unit,
    //'-> unit' is a lambda function. it executes when this action is triggered here it is 'edit' action. and doesn't return value
                     OnDeleteClick : () -> Unit) {
    //row used to display item name, qty.
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFF018786)),
                shape = RoundedCornerShape(20)
            ),
        //  .padding(8.dp) // Optional padding inside the Row
        horizontalArrangement = Arrangement.SpaceBetween
    ) {



        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Row {




                Text(text = item.name, modifier = Modifier.padding(8.dp))

                if(item.quantity >= 1){
                    Text(text = "Qty : ${item.quantity}", modifier = Modifier.padding(8.dp))
                }else{
                    Text(text = "Qty : 1", modifier = Modifier.padding(8.dp))
                }


            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)

                Text(text = "${item.address}")



            }
        }

        //nested row to add the delete and edit icons butoons
        Row(modifier = Modifier.padding(8.dp)) {
            //iconbutton to edit the added item.
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            //iconbutton for delete the added element.
            IconButton(onClick = OnDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }


    }

}


