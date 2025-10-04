package com.example.carggyappassignment.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carggyappassignment.models.VehicleRequest
import com.example.carggyappassignment.models.VehicleResponse
import com.example.carggyappassignment.network.Repository
import com.example.carggyappassignment.network.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class VehicleViewModel : ViewModel() {
    private val repository = Repository()

    var isLoading by mutableStateOf(false)
        private set

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    var vehicleList by mutableStateOf<List<VehicleResponse>>(emptyList())
        private set

    var vehicleDetails by mutableStateOf<VehicleResponse?>(null)
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

    suspend fun getVehicleDetails(vehicleId: Int) {
        try {
            isLoading = true

            when (val getVehicleDetails = repository.getVehicleDetails(vehicleId = vehicleId)) {
                is Resource.Success -> {
                    vehicleDetails = getVehicleDetails.data
                    isLoading = false
                }

                is Resource.Error -> {
                    vehicleDetails = null
                    isLoading = false
                }
            }

        } catch (e: Exception) {
            vehicleDetails = null
            isLoading = false
        }
    }

    suspend fun addNewVehicle(
        vehicleType: String,
        vehicleName: String,
        vehicleBrand: String,
        plateNo: String,
        year: String,
        selectedImageUri: Uri?,
    ): Boolean {
        return try {
            if (selectedImageUri == null) {
                return false
            }

            isLoading = true

            val createdDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            val imageName = "${UUID.randomUUID()}.jpg"
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("vehicleImages/$imageName")

            try {
                imageRef.putFile(selectedImageUri).await()
            } catch (e: Exception) {
                isLoading = false
                return false
            }

            val vehicleImageURL = imageRef.downloadUrl.await().toString()

            val vehicle = VehicleRequest(
                vehicleName = vehicleName,
                vehicleType = vehicleType,
                vehicleBrand = vehicleBrand,
                plateNo = plateNo,
                year = year,
                vehicleImageURL = vehicleImageURL,
                userId = userId ?: "",
                createdDate = createdDate
            )

            when (repository.postVehicle(vehicle = vehicle)) {
                is Resource.Success -> {
                    isLoading = false
                    true
                }

                is Resource.Error -> {
                    isLoading = false
                    false
                }
            }
        } catch (e: Exception) {
            isLoading = false
            false
        }
    }

    suspend fun editVehicle(
        vehicleId: Int,
        vehicleType: String,
        vehicleName: String,
        vehicleBrand: String,
        plateNo: String,
        year: String,
        selectedImageUri: Uri?
    ): Boolean{
        return try{
            isLoading = true

            var vehicleImageURL by mutableStateOf("")

            if(selectedImageUri == null){
                vehicleImageURL = vehicleDetails?.vehicleImageURL ?: ""
            }
            else{
                val imageName = "${UUID.randomUUID()}.jpg"
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("vehicleImages/$imageName")

                try {
                    imageRef.putFile(selectedImageUri).await()
                } catch (e: Exception) {
                    isLoading = false
                    return false
                }

                vehicleImageURL = imageRef.downloadUrl.await().toString()
            }

            val vehicle = VehicleResponse(
                vehicleId = vehicleId,
                vehicleType = vehicleType,
                vehicleName = vehicleName,
                vehicleBrand = vehicleBrand,
                plateNo = plateNo,
                year = year,
                vehicleImageURL = vehicleImageURL,
                userId = userId ?: "",
                createdDate = vehicleDetails?.createdDate ?: ""
            )

            when(repository.putVehicle(
                vehicleId = vehicleId,
                vehicle = vehicle
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
        catch (e: Exception){
            isLoading = false
            false
        }
    }

    suspend fun deleteVehicle(vehicleId: Int): Boolean {
        return try {
            isLoading = true

            when(repository.deleteVehicle(vehicleId = vehicleId)){
                is Resource.Success -> {
                    isLoading = false
                    true
                }

                is Resource.Error -> {
                    isLoading = false
                    false
                }
            }
        } catch (e: Exception) {
            isLoading = false
            false
        }
    }
}