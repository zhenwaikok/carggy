package com.example.carggyappassignment.models

import kotlinx.serialization.Serializable

@Serializable
data class ServiceLogRequest(
    var serviceName: String,
    var serviceType: String,
    var serviceDescription: String,
    var serviceDate: String,
    var servicePrice: String,
    var serviceImageURL: String,
    var vehicleId: Int,
    var createdDate: String,
)
