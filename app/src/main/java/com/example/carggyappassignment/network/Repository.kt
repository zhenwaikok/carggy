package com.example.carggyappassignment.network

import android.util.Log
import com.example.carggyappassignment.models.ApiResponseModel
import com.example.carggyappassignment.models.ServiceLogRequest
import com.example.carggyappassignment.models.ServiceLogResponse
import com.example.carggyappassignment.models.User
import com.example.carggyappassignment.models.VehicleRequest
import com.example.carggyappassignment.models.VehicleResponse

class Repository {

    //User
    suspend fun getUserDetails(userId: String): Resource<User> {
        return try{
            val response = ApiClient.apiService.getUserDetails(userId = userId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        }catch (e: Exception){
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun postUser(user: User): Resource<User> {
        return try {
            val response = ApiClient.apiService.postUser(user)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }


    suspend fun putUser(userId: String, user: User): Resource<ApiResponseModel> {
        return try {
            val response = ApiClient.apiService.putUser(
                userId = userId,
                user = user
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    //Vehicle
    suspend fun getVehicles(userId: String): Resource<List<VehicleResponse>> {
        return try {
            val response = ApiClient.apiService.getVehicle(
                userId = userId
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun getVehicleDetails(vehicleId: Int): Resource<VehicleResponse> {
        return try {
            val response = ApiClient.apiService.getVehicleDetails(
                vehicleId = vehicleId
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun postVehicle(vehicle: VehicleRequest): Resource<VehicleResponse> {
        return try {
            val response = ApiClient.apiService.postVehicle(
                vehicle = vehicle
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun putVehicle(vehicleId: Int,vehicle: VehicleResponse): Resource<ApiResponseModel> {
        return try {
            val response = ApiClient.apiService.putVehicle(
                vehicleId = vehicleId,
                vehicle = vehicle
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun deleteVehicle(vehicleId: Int): Resource<ApiResponseModel> {
        return try {
            val response = ApiClient.apiService.deleteVehicle(
                vehicleId = vehicleId,
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    //Service Log
    suspend fun getServiceLogs(vehicleId: Int): Resource<List<ServiceLogResponse>> {
        return try {
            val response = ApiClient.apiService.getServiceLog(
                vehicleId = vehicleId,
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun getServiceLogDetails(serviceLogId: Int): Resource<ServiceLogResponse> {
        return try {
            val response = ApiClient.apiService.getServiceLogDetails(
                serviceLogId = serviceLogId,
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun postServiceLog(serviceLog: ServiceLogRequest): Resource<ServiceLogResponse> {
        Log.d("VehicleViewModel", "Calling")

        return try {
            val response = ApiClient.apiService.postServiceLog(
                serviceLog = serviceLog
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun putServiceLog(serviceLogId: Int,serviceLog: ServiceLogResponse): Resource<ApiResponseModel> {
        return try {
            val response = ApiClient.apiService.putServiceLog(
                serviceLogId = serviceLogId,
                serviceLog = serviceLog
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    suspend fun deleteServiceLog(serviceLogId: Int): Resource<ApiResponseModel> {
        return try {
            val response = ApiClient.apiService.deleteServiceLog(
                serviceLogId = serviceLogId,
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }
}