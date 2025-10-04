package com.example.carggyappassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carggyappassignment.navigation.Routes
import com.example.carggyappassignment.screens.AccountDetailsScreen
import com.example.carggyappassignment.screens.AddServiceLogScreen
import com.example.carggyappassignment.screens.AddVehicleScreen
import com.example.carggyappassignment.screens.EditServiceLogScreen
import com.example.carggyappassignment.screens.EditVehicleScreen
import com.example.carggyappassignment.screens.HomeScreen
import com.example.carggyappassignment.screens.LoginScreen
import com.example.carggyappassignment.screens.ProfileScreen
import com.example.carggyappassignment.screens.RegisterScreen
import com.example.carggyappassignment.screens.ServiceLogDetailsScreen
import com.example.carggyappassignment.screens.VehicleDetailsScreen
import com.example.carggyappassignment.screens.VehicleScreen
import com.example.carggyappassignment.ui.theme.CarggyAppAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarggyAppAssignmentTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    val navigationController = rememberNavController()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
    ){ innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navigationController,
            startDestination = Routes.Login.route,
            builder =
            {
                composable(route = Routes.Login.route){
                    LoginScreen(navigationController)
                }

                composable(route = Routes.Register.route){
                    RegisterScreen(navigationController)
                }

                composable(route = Routes.Home.route){
                    HomeScreen(navigationController)
                }

                composable(route = Routes.Vehicle.route){
                    VehicleScreen(navigationController)
                }

                composable(route = Routes.Profile.route){
                    ProfileScreen(navigationController)
                }

                composable(route = Routes.VehicleDetails.route + "/{vehicleId}"){ backStackEntry ->
                    val vehicleId = backStackEntry.arguments?.getString("vehicleId")?.toInt()
                    VehicleDetailsScreen(navigationController, vehicleId ?: 0)
                }

                composable(route = Routes.ServiceLogDetails.route + "/{serviceLogId}"){ backStackEntry ->
                    val serviceLogId = backStackEntry.arguments?.getString("serviceLogId")?.toInt()
                    ServiceLogDetailsScreen(navigationController, serviceLogId ?: 0)
                }

                composable(route = Routes.AccountDetails.route){
                    AccountDetailsScreen(navigationController)
                }

                composable(route = Routes.AddVehicle.route){
                    AddVehicleScreen(navigationController)
                }

                composable(route = Routes.AddServiceLog.route + "/{vehicleId}"){ backStackEntry ->
                    val vehicleId = backStackEntry.arguments?.getString("vehicleId")?.toInt()
                    AddServiceLogScreen(navigationController, vehicleId = vehicleId ?: 0)
                }

                composable(route = Routes.EditVehicle.route + "/{vehicleId}"){ backStackEntry ->
                    val vehicleId = backStackEntry.arguments?.getString("vehicleId")?.toInt()
                   EditVehicleScreen(navigationController, vehicleId ?: 0)
                }

                composable(route = Routes.EditServiceLog.route + "/{serviceLogId}"){ backStackEntry ->
                    val serviceLogId = backStackEntry.arguments?.getString("serviceLogId")?.toInt()
                    EditServiceLogScreen(navigationController, serviceLogId ?: 0)
                }
            }
        )
    }
}
