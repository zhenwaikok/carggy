package com.example.carggyappassignment.widgets


import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.carggyappassignment.navigation.NavItem
import com.example.carggyappassignment.navigation.Routes
import com.example.carggyappassignment.ui.theme.Orange

@Composable
fun BottomNavigationBar(navController: NavController){

    val navigationItems = listOf(
        NavItem(
            title = "HOME",
            icon = Icons.Default.Home,
            route = Routes.Home.route
        ),
        NavItem(
            title = "VEHICLES",
            icon = Icons.Default.DirectionsCar,
            route = Routes.Vehicle.route
        ),
        NavItem(
            title = "PROFILE",
            icon = Icons.Default.Person,
            route = Routes.Profile.route
        )
    )

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    NavigationBar (
        containerColor = Color.White,
        modifier = Modifier.height(75.dp)
    ){
        navigationItems.forEachIndexed { index, navItem ->
            val isSelected = currentRoute == navItem.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.title,
                    )
                },
                label = {
                    Text(
                        text = navItem.title,
                        fontSize = 13.sp,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Orange,
                    selectedTextColor = Orange
                )

            )
        }
    }
}