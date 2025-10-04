package com.example.carggyappassignment.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.carggyappassignment.R
import com.example.carggyappassignment.navigation.Routes
import com.example.carggyappassignment.ui.theme.Orange
import com.example.carggyappassignment.viewmodel.ServiceLogViewModel
import com.example.carggyappassignment.widgets.TopBar

@Composable
fun ServiceLogDetailsScreen(
    navController: NavController,
    serviceLogId: Int,
    serviceLogVM: ServiceLogViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        serviceLogVM.getServiceLogDetails(serviceLogId = serviceLogId)
    }

    val serviceLogDetails = serviceLogVM.serviceLogDetails

    Scaffold (
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "SERVICE LOG DETAILS",
                containerColor = Orange,
                titleContentColor = Color.White,
                isNavigationIcon = true,
                navController = navController
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 0.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(20.dp))

            ServiceNameTitle(
                serviceLogId = serviceLogId,
                serviceName = serviceLogDetails?.serviceName ?: "-",
                navController = navController
            )

            Spacer(Modifier.height(30.dp))

            ServiceLogDetails(
                imageURL = serviceLogDetails?.serviceImageURL ?: "-",
                serviceType = serviceLogDetails?.serviceType ?: "-",
                serviceDescription = serviceLogDetails?.serviceDescription ?: "-",
                serviceDate = serviceLogDetails?.serviceDate ?: "-",
                servicePrice = "RM ${serviceLogDetails?.servicePrice ?: "-"}"
            )
        }
    }
}

@Composable
fun ServiceNameTitle(
    serviceLogId: Int,
    serviceName: String,
    navController: NavController
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = serviceName,
            fontSize = 30.sp,
            color = Orange,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        IconButton({
            navController.navigate(Routes.EditServiceLog.route + "/${serviceLogId}")
        }) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun ServiceLogDetails(
    imageURL: String?,
    serviceType: String,
    serviceDescription: String,
    serviceDate: String,
    servicePrice: String,
){
    Column {
        Text(
            text = "Receipt",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        AsyncImage(
            model = if(!imageURL.isNullOrEmpty()) imageURL else painterResource(R.drawable.placeholder_image),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder_image)
        )

        Spacer(Modifier.height(30.dp))

        ServiceLogDetailsText(
            title = "Service Type",
            description = serviceType
        )

        Spacer(Modifier.height(20.dp))

        ServiceLogDetailsText(
            title = "Service Description",
            description = serviceDescription
        )

        Spacer(Modifier.height(20.dp))

        ServiceLogDetailsText(
            title = "Service Date",
            description = serviceDate
        )

        Spacer(Modifier.height(20.dp))

        ServiceLogDetailsText(
            title = "Service Price",
            description = servicePrice
        )
    }
}

@Composable
fun ServiceLogDetailsText(title: String, description: String){
    Column {
        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = description,
            fontSize = 20.sp,
            color = Color.Gray
        )
    }
}


