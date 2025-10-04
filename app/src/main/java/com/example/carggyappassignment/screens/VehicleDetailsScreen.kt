package com.example.carggyappassignment.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.carggyappassignment.R
import com.example.carggyappassignment.models.ServiceLogResponse
import com.example.carggyappassignment.models.VehicleResponse
import com.example.carggyappassignment.navigation.Routes
import com.example.carggyappassignment.ui.theme.Orange
import com.example.carggyappassignment.viewmodel.ServiceLogViewModel
import com.example.carggyappassignment.viewmodel.VehicleViewModel
import com.example.carggyappassignment.widgets.BottomContextMenu
import com.example.carggyappassignment.widgets.CustomSearchBar
import com.example.carggyappassignment.widgets.CustomTextField
import com.example.carggyappassignment.widgets.LoadingIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun VehicleDetailsScreen(
    navController: NavController,
    vehicleId: Int,
    vehicleVM:VehicleViewModel = viewModel(),
    serviceLogVM: ServiceLogViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        vehicleVM.getVehicleDetails(vehicleId = vehicleId)
        serviceLogVM.getServiceLogs(vehicleId = vehicleId)
    }

    val vehicleDetails = vehicleVM.vehicleDetails
    val serviceLogs = serviceLogVM.serviceLogsList

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val searchQuery = remember { mutableStateOf("") }

    val dateFilter = remember { mutableStateOf("All") }

    val filteredServiceLogList = serviceLogs.filter {
        (
            searchQuery.value.isEmpty() ||
            it.serviceName?.contains(searchQuery.value, ignoreCase = true) == true ||
            it.serviceType?.contains(searchQuery.value, ignoreCase = true) == true ||
            it.servicePrice?.contains(searchQuery.value, ignoreCase = true) == true)
            &&
            serviceLogVM.isWithinPeriodRange(it.serviceDate, dateFilter.value
        )
    }

    Scaffold (
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (vehicleDetails != null) {
                item {

                    VehicleImage(
                        vehicleDetails = vehicleDetails,
                        navController = navController
                    )
                    Spacer(Modifier.height(20.dp))
                }

                item {
                    VehicleDetails(
                        vehicleDetails = vehicleDetails,
                        navController = navController,
                        vehicleId = vehicleDetails.vehicleId ?: 0
                    )

                    Spacer(Modifier.height(40.dp))
                }

                item{
                    ServiceLogs(
                        serviceLogs = serviceLogs,
                        filterServiceLogs = filteredServiceLogList,
                        navController = navController,
                        vehicleId = vehicleId,
                        coroutineScope = coroutineScope,
                        serviceLogVM = serviceLogVM,
                        context = context,
                        searchQuery = searchQuery,
                        dateFilter = dateFilter
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleImage(vehicleDetails: VehicleResponse, navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AsyncImage(
            model = vehicleDetails.vehicleImageURL ?: "",
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(
                R.drawable.placeholder_image,
            )
        )

        IconButton(
                {navController.popBackStack()},
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(
                        color = Orange,
                        shape = CircleShape
                    )
            ) {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun VehicleDetails(
    vehicleDetails: VehicleResponse,
    navController: NavController,
    vehicleId: Int
){
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Vehicle Details",
                fontSize = 30.sp,
                color = Orange,
                fontWeight = FontWeight.Bold
            )

            IconButton({
                navController.navigate(Routes.EditVehicle.route + "/$vehicleId")
            }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        VehicleDetailsText(
            text = vehicleDetails.vehicleName ?: "",
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(Modifier.height(10.dp))

        VehicleDetailsText(
            text = "Type: ${vehicleDetails.vehicleType ?: ""}",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(10.dp))

        VehicleDetailsText(
            text = "Brand: ${vehicleDetails.vehicleBrand ?: ""}",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(10.dp))

        VehicleDetailsText(
            text = "Plate No: ${vehicleDetails.plateNo ?: ""}",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(10.dp))

        VehicleDetailsText(
            text = "Year: ${vehicleDetails.year ?: ""}",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ServiceLogs(
    serviceLogs: List<ServiceLogResponse>,
    filterServiceLogs: List<ServiceLogResponse>,
    navController: NavController,
    vehicleId: Int,
    coroutineScope: CoroutineScope,
    serviceLogVM: ServiceLogViewModel,
    context: Context,
    searchQuery: MutableState<String>,
    dateFilter: MutableState<String>
){
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Service Logs",
                fontSize = 30.sp,
                color = Orange,
                fontWeight = FontWeight.Bold
            )

            IconButton({
                navController.navigate(Routes.AddServiceLog.route + "/$vehicleId")
            }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        if(serviceLogs.isNotEmpty()){
            CustomSearchBar(
                searchQuery = searchQuery
            )

            Spacer(Modifier.height(20.dp))
            
            DateFilterDropdown(
                selectedFilter = dateFilter.value,
                onFilterSelected = { selectedFilter ->
                    dateFilter.value = selectedFilter
                }
            )

            Spacer(Modifier.height(20.dp))

            if(filterServiceLogs.isNotEmpty()){
                ServiceLogsList(
                    serviceLogs = filterServiceLogs,
                    navController = navController,
                    coroutineScope = coroutineScope,
                    serviceLogVM = serviceLogVM,
                    context = context,
                    vehicleId = vehicleId
                )

                Spacer(Modifier.height(20.dp))
            }else{
                NoServiceLogResultTitle()
            }
        }else{
            Text(
                text = "Opps, currently no service log yet.",
                fontSize = 20.sp,
            )
        }


    }
}

@Composable
fun ServiceLogsList(
    serviceLogs: List<ServiceLogResponse>,
    navController: NavController,
    coroutineScope: CoroutineScope,
    serviceLogVM: ServiceLogViewModel,
    context: Context,
    vehicleId: Int
){
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        serviceLogs.forEach { serviceLog ->
            ServiceLogsCard(
                navController = navController,
                serviceLogId = serviceLog.serviceLogId ?: 0,
                serviceName = serviceLog.serviceName ?: "",
                serviceType = serviceLog.serviceType ?: "",
                serviceDate = serviceLog.serviceDate ?: "",
                servicePrice = serviceLog.servicePrice ?: "",
                onDeleteClick = {
                    coroutineScope.launch {
                        val result = serviceLogVM.deleteServiceLog(serviceLog.serviceLogId ?: 0)

                        if(result){
                            serviceLogVM.getServiceLogs(vehicleId = vehicleId)
                            Toast.makeText(context, "Successfully removed!", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Failed to remove, please try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onEditClick = {
                    navController.navigate(Routes.EditServiceLog.route + "/${serviceLog.serviceLogId ?: 0}")
                }
            )
        }
    }
}

@Composable
fun ServiceLogsCard(
    navController: NavController,
    serviceLogId: Int,
    serviceName: String,
    serviceType: String,
    serviceDate: String,
    servicePrice: String,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
){
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        expanded.value = true
                    },
                    onTap = {
                        navController.navigate(Routes.ServiceLogDetails.route + "/$serviceLogId")
                    }
                )
            }
            .height(90.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(10.dp)
    ){
        Column (
            verticalArrangement = Arrangement.Center
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Name: $serviceName",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(25.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Orange),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = serviceType,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Date: $serviceDate",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Price: RM$servicePrice",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if(expanded.value){
                BottomContextMenu(
                    expanded = expanded,
                    onDeleteClick = onDeleteClick,
                    onEditClick = onEditClick
                )
            }
        }

    }
}

@Composable
fun VehicleDetailsText(text: String, fontSize: TextUnit, color: Color){
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
fun NoServiceLogResultTitle(){
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilterDropdown(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filterOptions = listOf(
        "All",
        "Past 7 Days",
        "Past 14 Days",
        "Past 1 Month",
        "Past 3 Months",
        "Past 6 Months",
        "Past 1 Year",
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            CustomTextField(
                modifier = Modifier.menuAnchor(),
                textTitle = "Filter by Period",
                inputValue = selectedFilter,
                onChangeValue = {},
                isPassword = false,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontSize = 18.sp) },
                        onClick = {
                            onFilterSelected(option)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

