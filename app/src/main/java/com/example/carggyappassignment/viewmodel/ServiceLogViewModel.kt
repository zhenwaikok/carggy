package com.example.carggyappassignment.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.carggyappassignment.models.ServiceLogRequest
import com.example.carggyappassignment.models.ServiceLogResponse
import com.example.carggyappassignment.models.VehicleResponse
import com.example.carggyappassignment.network.Repository
import com.example.carggyappassignment.network.Resource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class ServiceLogViewModel:ViewModel() {

    private val repository = Repository()

    var isLoading by mutableStateOf(false)
        private set

    var serviceLogsList by mutableStateOf<List<ServiceLogResponse>>(emptyList())
        private set

    var latestServiceLogList by mutableStateOf<List<ServiceLogResponse>>(emptyList())
        private set

    var serviceLogDetails by mutableStateOf<ServiceLogResponse?>(null)
        private set

    suspend fun getServiceLogs(vehicleId: Int){
        try{
            isLoading = true

            when(val response = repository.getServiceLogs(vehicleId = vehicleId))
            {
                is Resource.Success -> {
                    serviceLogsList = response.data ?: emptyList()
                    isLoading = false
                }

                is Resource.Error -> {
                    serviceLogsList = emptyList()
                    isLoading = false
                }
            }
        }catch (e:Exception){
            serviceLogsList = emptyList()
            isLoading = false
        }
    }

    suspend fun getServiceLogDetails(serviceLogId: Int){
        try{
            when (val response = repository.getServiceLogDetails(serviceLogId = serviceLogId)){
                is Resource.Success -> {
                    serviceLogDetails = response.data
                }

                is Resource.Error -> {
                    serviceLogDetails = null
                }
            }
        }catch (e:Exception){
            serviceLogDetails = null
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
                                LocalDateTime.parse(logs.serviceDate, dateFormatter)
                            }catch (e:Exception) {
                                LocalDateTime.MIN
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

    suspend fun addServiceLog(
        serviceName: String,
        serviceType: String,
        serviceDescription: String,
        serviceDate: String,
        servicePrice: String,
        selectedImageUri: Uri?,
        vehicleId: Int
    ): Boolean{
        return try{
            if(selectedImageUri == null){
                return false
            }

            isLoading = true

            val createdDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            val imageName = "${UUID.randomUUID()}.jpg"
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("serviceLogImages/$imageName")

            try{
                imageRef.putFile(selectedImageUri).await()
            }catch (e:Exception){
                isLoading = false
                return false
            }

            val serviceImageURL = imageRef.downloadUrl.await().toString()

            val serviceLog = ServiceLogRequest(
                serviceName = serviceName,
                serviceType = serviceType,
                serviceDescription = serviceDescription,
                serviceDate = serviceDate,
                servicePrice = servicePrice,
                serviceImageURL = serviceImageURL,
                vehicleId = vehicleId,
                createdDate = createdDate
            )

            when(repository.postServiceLog(
                serviceLog = serviceLog
            )){
                is Resource.Success -> {
                    isLoading = false
                    true
                }

                is Resource.Error -> {
                    isLoading = false
                    false
                }

            }
        }
        catch (e:Exception){
            isLoading = false
            false
        }
    }

    suspend fun editServiceLog(
        serviceLogId: Int,
        serviceName: String,
        serviceType: String,
        serviceDescription: String,
        serviceDate: String,
        servicePrice: String,
        selectedImageUri: Uri?,
        vehicleId: Int
    ): Boolean{
        return try {
            var serviceImageURL by mutableStateOf("")

            isLoading = true

            if(selectedImageUri == null){
                serviceImageURL = serviceLogDetails?.serviceImageURL ?: ""
            }else{
                val imageName = "${UUID.randomUUID()}.jpg"
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("serviceLogImages/$imageName")

                try{
                    imageRef.putFile(selectedImageUri).await()
                }catch (e:Exception){
                    isLoading = false
                    return false
                }

                serviceImageURL = imageRef.downloadUrl.await().toString()
            }

            val serviceLog = ServiceLogResponse(
                serviceLogId = serviceLogId,
                serviceName = serviceName,
                serviceType = serviceType,
                serviceDescription = serviceDescription,
                serviceDate = serviceDate,
                servicePrice = servicePrice,
                serviceImageURL = serviceImageURL,
                vehicleId = vehicleId,
                createdDate = serviceLogDetails?.createdDate
            )

            when(repository.putServiceLog(
                serviceLogId = serviceLogId,
                serviceLog = serviceLog
            )){
                is Resource.Success -> {
                    isLoading = false
                    true
                }

                is Resource.Error -> {
                    isLoading = false
                    false
                }
            }
        }catch (e:Exception){
            isLoading = false
            false
        }
    }

    suspend fun deleteServiceLog(serviceLogId: Int): Boolean{
        return try {
            isLoading = true

            when(repository.deleteServiceLog(serviceLogId =  serviceLogId)){
                is Resource.Success -> {
                    isLoading = false
                    true
                }

                is Resource.Error -> {
                    isLoading = false
                    false
                }
            }
        }
        catch (e:Exception){
            isLoading = false
            false
        }
    }

    fun isWithinPeriodRange(serviceDate: String?, filter: String): Boolean {
        if (serviceDate.isNullOrEmpty()) return false
        if (filter == "All") return true

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = try {
            formatter.parse(serviceDate)
        } catch (e: Exception) {
            return false
        } ?: return false

        val calendar = Calendar.getInstance()

        when (filter) {
            "Past 7 Days" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
            }
            "Past 14 Days" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -14)
            }
            "Past 1 Month" -> {
                calendar.add(Calendar.MONTH, -1)
            }
            "Past 3 Months" -> {
                calendar.add(Calendar.MONTH, -3)
            }
            "Past 6 Months" -> {
                calendar.add(Calendar.MONTH, -6)
            }
            "Past 1 Year" -> {
                calendar.add(Calendar.YEAR, -1)
            }
            else -> return true
        }

        return parsedDate.after(calendar.time) || parsedDate == calendar.time
    }

}