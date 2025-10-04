package com.example.carggyappassignment.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseModel(
    var status: Int?,
    var message: String?,
)
