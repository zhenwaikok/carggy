package com.example.carggyappassignment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carggyappassignment.models.ServiceLogResponse
import com.example.carggyappassignment.models.VehicleResponse
import com.example.carggyappassignment.network.Repository
import com.example.carggyappassignment.network.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel:ViewModel() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val repository = Repository()

    var isLoading by mutableStateOf(false)
        private set

    var latestServiceLogList by mutableStateOf<List<ServiceLogResponse>>(emptyList())
        private set

    var vehicleList by mutableStateOf<List<VehicleResponse>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            getVehicleList()
        }
    }

    suspend fun getVehicleList() {
        try {
            isLoading = true

            when (val getVehicles = repository.getVehicles(userId = userId ?: "")) {
                is Resource.Success -> {
                    vehicleList = getVehicles.data ?: emptyList()
                    getLatestServiceLog(vehicleList = vehicleList)
                    isLoading = false
                }

                is Resource.Error -> {
                    vehicleList = emptyList()
                    isLoading = false
                }
            }
        } catch (e: Exception) {
            vehicleList = emptyList()
            isLoading = false
        }
    }

    suspend fun getLatestServiceLog(vehicleList: List<VehicleResponse>){
        try{
            isLoading = true
            val latestLogs = mutableListOf<ServiceLogResponse>()

            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            for (vehicle in vehicleList){
                when(val response = repository.getServiceLogs(vehicleId = vehicle.vehicleId ?: 0)){
                    is Resource.Success -> {
                        val serviceLogs = response.data

                        val latestServiceLogs = serviceLogs?.maxByOrNull { logs ->
                            try{
                                LocalDate.parse(logs.serviceDate, dateFormatter)
                            }catch (e:Exception) {
                                LocalDate.MIN
                            }
                        }

                        latestServiceLogs?.let { latestLogs.add(it) }
                    }

                    is Resource.Error -> {

                    }
                }
            }

            latestServiceLogList = latestLogs
            isLoading = false
        }catch (e: Exception){
            latestServiceLogList = emptyList()
            isLoading = false
        }
    }

    fun getUpcomingServiceDate(vehicleId: Int): String? {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val latestVehicleServiceLog = latestServiceLogList
            .filter { it.vehicleId == vehicleId }
            .maxByOrNull {
                try{
                    LocalDate.parse(it.serviceDate, dateFormatter)
                }catch (e:Exception){
                    LocalDate.MIN
                }
            }

        return latestVehicleServiceLog?.let {
            val latestVehicleServiceDate = try{
                LocalDate.parse(it.serviceDate, dateFormatter)
            }catch (e:Exception){
                LocalDate.MIN
            }

            val serviceType = it.serviceType ?:""

            val intervalMonths = when(serviceType){
                "Minor" -> 3L
                "Major" -> 6L
                "Maintenance" -> 9L
                "Check" -> 2L
                else -> 0L
            }

            val upcomingServiceDate = latestVehicleServiceDate.plusMonths(intervalMonths)

            upcomingServiceDate.format(dateFormatter)
        }
    }
}