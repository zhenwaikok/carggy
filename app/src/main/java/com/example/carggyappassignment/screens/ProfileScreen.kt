package com.example.carggyappassignment.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.carggyappassignment.R
import com.example.carggyappassignment.navigation.Routes
import com.example.carggyappassignment.ui.theme.Orange
import com.example.carggyappassignment.viewmodel.ProfileViewModel
import com.example.carggyappassignment.widgets.BottomNavigationBar
import com.example.carggyappassignment.widgets.LoadingIndicator
import com.example.carggyappassignment.widgets.TopBar

@Composable
fun ProfileScreen(
    navController: NavController,
    profileVM: ProfileViewModel = viewModel()
){
    val isLoading = profileVM.isLoading
    val userDetails = profileVM.userDetails
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        profileVM.getUserDetails()
    }

    Scaffold (
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "PROFILE",
                containerColor = Orange,
                titleContentColor = Color.White,
                isNavigationIcon = false
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ){
            if(isLoading){
                LoadingIndicator()
            }
            else{
                Spacer(Modifier.height(40.dp))

                ProfileDetails(
                    imageURL = userDetails?.imageURL ?: "",
                    username = userDetails?.username ?: ""
                )

                Spacer(Modifier.height(30.dp))

                ProfileNavigation(
                    navController,
                    profileVM = profileVM,
                    context = context
                )
            }
        }
    }
}

@Composable
fun ProfileDetails(imageURL: String?, username: String){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = if(!imageURL.isNullOrEmpty()) imageURL else R.drawable.profile_image,
            contentDescription = "",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.profile_image),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = username,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileNavigation(
    navController: NavController,
    profileVM: ProfileViewModel,
    context: Context
){
    Column {
        ProfileRow(
            title = "Account Details",
            icon = Icons.Default.AccountBox,
            onClick = {
                navController.navigate(Routes.AccountDetails.route)
            }
        )

        Spacer(Modifier.height(40.dp))

        ProfileRow(
            title = "Logout",
            icon = Icons.AutoMirrored.Default.ExitToApp,
            onClick = {
                val result = profileVM.signOut()
                
                if(result){
                    Toast.makeText(context,"Logout successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.Login.route){
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}

@Composable
fun ProfileRow(title: String, icon: ImageVector,onClick: () -> Unit){
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                icon,
                contentDescription = "",
                tint = Orange,
                modifier = Modifier.size(50.dp)
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = title,
                fontSize = 20.sp,
            )
        }

        Icon(
            Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = ""
        )
    }
}