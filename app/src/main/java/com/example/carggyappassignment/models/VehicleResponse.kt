package com.example.carggyappassignment.models

import kotlinx.serialization.Serializable

@Serializable
data class VehicleResponse(
    var vehicleId: Int?,
    var vehicleName: String?,
    var vehicleType: String?,
    var vehicleBrand: String?,
    var plateNo: String?,
    var year: String?,
    var vehicleImageURL: String?,
    var userId: String?,
    var createdDate: String?,
)
