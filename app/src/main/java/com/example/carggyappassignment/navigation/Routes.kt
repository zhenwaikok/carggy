package com.example.carggyappassignment.navigation

sealed class Routes(val route: String) {
    object Login: Routes("LoginScreen")
    object Register: Routes("RegisterScreen")
    object Home: Routes("HomeScreen")
    object Vehicle: Routes("VehicleScreen")
    object Profile: Routes("ProfileScreen")
    object VehicleDetails: Routes("VehicleDetailsScreen")
    object ServiceLogDetails: Routes("ServiceLogDetailsScreen")
    object AccountDetails: Routes("AccountDetailsScreen")
    object AddVehicle: Routes("AddVehicleScreen")
    object AddServiceLog: Routes("AddServiceLogScreen")
    object EditVehicle: Routes("EditVehicleScreen")
    object EditServiceLog: Routes("EditServiceLogScreen")
}