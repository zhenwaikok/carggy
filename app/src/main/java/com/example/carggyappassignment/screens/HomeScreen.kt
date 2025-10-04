package com.example.carggyappassignment.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
import com.example.carggyappassignment.viewmodel.HomeViewModel
import com.example.carggyappassignment.viewmodel.ProfileViewModel
import com.example.carggyappassignment.viewmodel.ServiceLogViewModel
import com.example.carggyappassignment.viewmodel.VehicleViewModel
import com.example.carggyappassignment.widgets.BottomContextMenu
import com.example.carggyappassignment.widgets.BottomNavigationBar
import com.example.carggyappassignment.widgets.LoadingIndicator
import com.example.carggyappassignment.widgets.TopBar
import com.example.carggyappassignment.widgets.VehicleCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    profileVM: ProfileViewModel = viewModel(),
    vehicleVM: VehicleViewModel = viewModel(),
    homeVM: HomeViewModel = viewModel(),
    serviceLogVM: ServiceLogViewModel = viewModel()
) {
    val isLoading = homeVM.isLoading

    val userDetails = profileVM.userDetails
    val vehicleList = homeVM.vehicleList

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val latestServiceLogList = homeVM.latestServiceLogList

    LaunchedEffect(Unit) {
        homeVM.getLatestServiceLog(vehicleList = vehicleList)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "HOME",
                containerColor = Orange,
                titleContentColor = Color.White,
                isNavigationIcon = false
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(15.dp))

                WelcomeUser(
                    username = userDetails?.username ?: "",
                    imageURL = userDetails?.imageURL ?: ""
                )

                Spacer(Modifier.height(40.dp))

                UpcomingService(
                    latestServiceLogList = latestServiceLogList,
                    vehicleList = vehicleList,
                    homeVM = homeVM
                )

                Spacer(Modifier.height(40.dp))

                MyVehicles(
                    vehicleList = vehicleList,
                    navController = navController,
                    vehicleVM = vehicleVM,
                    homeVM = homeVM,
                    coroutineScope = coroutineScope,
                    context = context
                )

                Spacer(Modifier.height(40.dp))

                LatestService(
                    latestServiceLogList = latestServiceLogList,
                    vehicleList = vehicleList,
                    navController = navController,
                    serviceLogVM = serviceLogVM,
                    coroutineScope = coroutineScope,
                    context = context,
                    homeVM = homeVM
                )

                Spacer(Modifier.height(25.dp))
            }
        }
    }
}

@Composable
fun WelcomeUser(username: String, imageURL: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "WELCOME, $username",
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        AsyncImage(
            model = if (!imageURL.isNullOrEmpty()) imageURL else R.drawable.profile_image,
            contentDescription = "",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.profile_image),
            contentScale = ContentScale.Crop,
        )

    }
}

@Composable
fun UpcomingService(
    latestServiceLogList: List<ServiceLogResponse>,
    vehicleList: List<VehicleResponse>,
    homeVM: HomeViewModel
) {
    Column() {
        Text(
            text = "Upcoming Service",
            fontSize = 20.sp,
            color = Orange,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(15.dp))

        UpcomingServiceList(
            latestServiceLogList = latestServiceLogList,
            vehicleList = vehicleList,
            homeVM = homeVM
        )
    }
}

@Composable
fun UpcomingServiceList(
    latestServiceLogList: List<ServiceLogResponse>,
    vehicleList: List<VehicleResponse>,
    homeVM: HomeViewModel
) {
    if (latestServiceLogList.isEmpty()) {
        NoData(
            text = "You have no upcoming service yet."
        )
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 5.dp)
        ) {
            items(latestServiceLogList) { latestLog ->
                val vehicleNo =
                    vehicleList.firstOrNull { it.vehicleId == latestLog.vehicleId }?.plateNo ?: ""
                val upcomingDate = homeVM.getUpcomingServiceDate(latestLog.vehicleId ?: 0)

                UpcomingServiceCard(
                    vehicleNo = vehicleNo,
                    upcomingDate = upcomingDate ?: ""
                )
            }
        }
    }
}

@Composable
fun UpcomingServiceCard(
    vehicleNo: String,
    upcomingDate: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(10.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Vehicle No: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = vehicleNo,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Next service date: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = upcomingDate,
                    fontSize = 16.sp
                )
            }
        }

    }
}

@Composable
fun MyVehicles(
    vehicleList: List<VehicleResponse>,
    navController: NavController,
    vehicleVM: VehicleViewModel,
    homeVM: HomeViewModel,
    coroutineScope: CoroutineScope,
    context: Context
) {
    Column() {
        Text(
            text = "My Vehicles",
            fontSize = 20.sp,
            color = Orange,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(15.dp))

        VehiclesList(
            vehicleList = vehicleList,
            navController = navController,
            vehicleVM = vehicleVM,
            homeVM = homeVM,
            coroutineScope = coroutineScope,
            context = context
        )
    }
}

@Composable
fun VehiclesList(
    vehicleList: List<VehicleResponse>,
    navController: NavController,
    vehicleVM: VehicleViewModel,
    homeVM: HomeViewModel,
    coroutineScope: CoroutineScope,
    context: Context
) {
    if (vehicleList.isEmpty()) {
        NoData(
            text = "You have no any vehicle yet."
        )
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 5.dp)
        ) {
            items(vehicleList) { vehicleItems ->
                VehicleCard(
                    imageURL = vehicleItems.vehicleImageURL ?: "",
                    vehicleNo = vehicleItems.plateNo ?: "",
                    vehicleName = vehicleItems.vehicleName ?: "",
                    onClick = {
                        navController.navigate(Routes.VehicleDetails.route + "/${vehicleItems.vehicleId}")
                    },
                    onEditClick = {
                        navController.navigate(Routes.EditVehicle.route + "/${vehicleItems.vehicleId}")
                    },
                    onDeleteClick = {
                        coroutineScope.launch {
                            val result = vehicleVM.deleteVehicle(vehicleItems.vehicleId ?: 0)

                            if (result) {
                                Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT)
                                    .show()

                                homeVM.getVehicleList()
                            } else {
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
}

@Composable
fun LatestService(
    latestServiceLogList: List<ServiceLogResponse>,
    vehicleList: List<VehicleResponse>,
    navController: NavController,
    serviceLogVM: ServiceLogViewModel,
    coroutineScope: CoroutineScope,
    context: Context,
    homeVM: HomeViewModel
) {
    Column() {
        Text(
            text = "Latest Service",
            fontSize = 20.sp,
            color = Orange,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(15.dp))

        LatestServiceLogList(
            latestServiceLogList = latestServiceLogList,
            vehicleList = vehicleList,
            navController = navController,
            serviceLogVM = serviceLogVM,
            coroutineScope = coroutineScope,
            context = context,
            homeVM = homeVM
        )
    }
}

@Composable
fun LatestServiceLogList(
    latestServiceLogList: List<ServiceLogResponse>,
    vehicleList: List<VehicleResponse>,
    navController: NavController,
    serviceLogVM: ServiceLogViewModel,
    coroutineScope: CoroutineScope,
    context: Context,
    homeVM: HomeViewModel
) {
    if (latestServiceLogList.isEmpty()) {
        NoData(
            text = "You have no any latest car service yet."
        )
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 5.dp)
        ) {
            items(latestServiceLogList) { serviceLogs ->
                val vehicle = vehicleList.find { it.vehicleId == serviceLogs.vehicleId }
                val vehicleNo = vehicle?.plateNo ?: "-"

                LatestServiceLogCard(
                    vehicleNo = vehicleNo,
                    serviceName = serviceLogs.serviceName ?: "-",
                    serviceType = serviceLogs.serviceType ?: "-",
                    serviceDate = serviceLogs.serviceDate ?: "-",
                    serviceLogId = serviceLogs.serviceLogId ?: 0,
                    navController = navController,
                    onDeleteClick = {
                        coroutineScope.launch {
                            val result =
                                serviceLogVM.deleteServiceLog(serviceLogs.serviceLogId ?: 0)

                            if (result) {
                                homeVM.getLatestServiceLog(vehicleList = vehicleList)
                                Toast.makeText(context, "Successfully removed!", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to remove, please try again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onEditClick = {
                        navController.navigate(Routes.ServiceLogDetails.route + "/${serviceLogs.serviceLogId}")
                    }
                )
            }
        }
    }
}

@Composable
fun LatestServiceLogCard(
    vehicleNo: String,
    serviceName: String,
    serviceLogId: Int,
    serviceType: String,
    serviceDate: String,
    navController: NavController,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(250.dp)
            .height(130.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(10.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        expanded.value = true
                    },
                    onTap = {
                        navController.navigate(Routes.ServiceLogDetails.route + "/$serviceLogId")
                    }
                )
            },
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = vehicleNo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
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

            Text(
                text = serviceName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Latest service: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = serviceDate,
                    fontSize = 16.sp
                )

            }
        }

        if (expanded.value) {
            BottomContextMenu(
                expanded = expanded,
                onDeleteClick = onDeleteClick,
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
fun NoData(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
        )
    }
}