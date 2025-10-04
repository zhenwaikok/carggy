package com.example.carggyappassignment.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carggyappassignment.models.VehicleResponse
import com.example.carggyappassignment.navigation.Routes
import com.example.carggyappassignment.ui.theme.Orange
import com.example.carggyappassignment.viewmodel.VehicleViewModel
import com.example.carggyappassignment.widgets.BottomNavigationBar
import com.example.carggyappassignment.widgets.CustomSearchBar
import com.example.carggyappassignment.widgets.LoadingIndicator
import com.example.carggyappassignment.widgets.TopBar
import com.example.carggyappassignment.widgets.VehicleCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun VehicleScreen(navController: NavController, vehicleVM:VehicleViewModel = viewModel()){
    val vehicleList = vehicleVM.vehicleList
    val isLoading = vehicleVM.isLoading

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val searchQuery = remember { mutableStateOf("") }

    val filteredVehicleList = if (searchQuery.value.isEmpty()){
        vehicleList
    } else{
        vehicleList.filter {
            it.vehicleName?.contains(searchQuery.value, ignoreCase = true) == true ||
            it.plateNo?.contains(searchQuery.value, ignoreCase = true) == true
        }
    }

    LaunchedEffect(Unit) {
        vehicleVM.getVehicleList()
    }

    Scaffold (
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "VEHICLE",
                containerColor = Orange,
                titleContentColor = Color.White,
                isNavigationIcon = false
            )
        },
        floatingActionButton = {
            FloatingButton(
                navController = navController
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
        ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(15.dp)
        )
        {
            if(isLoading){
                LoadingIndicator()
            }else if(vehicleList.isNotEmpty()) {
                CustomSearchBar(
                    searchQuery = searchQuery,
                )

                Spacer(Modifier.height(20.dp))

                if(filteredVehicleList.isNotEmpty()){
                    VehicleList(
                        vehicles = filteredVehicleList,
                        navController = navController,
                        vehicleVM = vehicleVM,
                        coroutineScope = coroutineScope,
                        context = context
                    )
                }else{
                    NoVehicleResultTitle()
                }
            }else{
                NoDataTitle()
            }
        }
    }
}

@Composable
fun VehicleList(
    vehicles: List<VehicleResponse>,
    navController: NavController,
    vehicleVM: VehicleViewModel,
    coroutineScope: CoroutineScope,
    context:Context
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(vehicles){ vehicle ->
            VehicleCard(
                imageURL = vehicle.vehicleImageURL ?: "",
                vehicleNo = vehicle.plateNo ?: "",
                vehicleName = vehicle.vehicleName ?: "",
                onClick = ({
                    navController.navigate(
                    Routes.VehicleDetails.route + "/${vehicle.vehicleId}")
                }),
                onEditClick = ({
                    navController.navigate(
                        Routes.EditVehicle.route + "/${vehicle.vehicleId}")
                }),
                onDeleteClick = {
                    coroutineScope.launch {
                        val result = vehicleVM.deleteVehicle(vehicleId = vehicle.vehicleId ?: 0)

                        if(result){
                            Toast.makeText(
                                context,
                                "Successfully removed!",
                                Toast.LENGTH_SHORT
                            ).show()

                            vehicleVM.getVehicleList()
                        }else{
                            Toast.makeText(
                                context,
                                "Failed to remove, please try again!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FloatingButton(
    navController: NavController
){
    FloatingActionButton(
        onClick = {
            navController.navigate(Routes.AddVehicle.route)
        },
        containerColor = Orange,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription =  ""
        )
    }
}

@Composable
fun NoDataTitle(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Opps, you have no any vehicle yet.",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoVehicleResultTitle(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No result found.",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}