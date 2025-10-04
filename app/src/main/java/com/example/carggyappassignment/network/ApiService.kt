package com.example.carggyappassignment.network

import com.example.carggyappassignment.models.ApiResponseModel
import com.example.carggyappassignment.models.ServiceLogRequest
import com.example.carggyappassignment.models.ServiceLogResponse
import com.example.carggyappassignment.models.User
import com.example.carggyappassignment.models.VehicleRequest
import com.example.carggyappassignment.models.VehicleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //User
    @GET("api/User/{UserId}")
    suspend fun getUserDetails(
        @Path("UserId") userId : String
    ): Response<User>

    @POST("api/User")
    suspend fun postUser(
        @Body user: User
    ): Response<User>

    @PUT("api/User/{UserId}")
    suspend fun putUser(
        @Path("UserId") userId: String,
        @Body user: User
    ): Response<ApiResponseModel>

    //Vehicle
    @GET("api/Vehicle/{UserId}")
    suspend fun getVehicle(
        @Path("UserId") userId: String
    ): Response<List<VehicleResponse>>

    @GET("api/Vehicle/{VehicleId}")
    suspend fun getVehicleDetails(
        @Path("VehicleId") vehicleId: Int
    ): Response<VehicleResponse>

    @POST("api/Vehicle")
    suspend fun postVehicle(
        @Body vehicle: VehicleRequest
    ): Response<VehicleResponse>

    @PUT("api/Vehicle/{VehicleId}")
    suspend fun putVehicle(
        @Path("VehicleId") vehicleId: Int,
        @Body vehicle: VehicleResponse
    ): Response<ApiResponseModel>

    @DELETE("api/Vehicle/{VehicleId}")
    suspend fun deleteVehicle(
        @Path("VehicleId") vehicleId: Int
    ): Response<ApiResponseModel>

    //Service Log
    @GET("api/ServiceLog/vehicle/{VehicleId}")
    suspend fun getServiceLog(
        @Path("VehicleId") vehicleId: Int
    ): Response<List<ServiceLogResponse>>

    @GET("api/ServiceLog/{ServiceLogId}")
    suspend fun getServiceLogDetails(
        @Path("ServiceLogId") serviceLogId: Int
    ): Response<ServiceLogResponse>

    @POST("api/ServiceLog")
    suspend fun postServiceLog(
        @Body serviceLog: ServiceLogRequest
    ): Response<ServiceLogResponse>

    @PUT("api/ServiceLog/{ServiceLogId}")
    suspend fun putServiceLog(
        @Path("ServiceLogId") serviceLogId: Int,
        @Body serviceLog: ServiceLogResponse
    ): Response<ApiResponseModel>

    @DELETE("api/ServiceLog/{ServiceLogId}")
    suspend fun deleteServiceLog(
        @Path("ServiceLogId") serviceLogId: Int
    ): Response<ApiResponseModel>
}