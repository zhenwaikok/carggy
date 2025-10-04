package com.example.carggyappassignment.models

import kotlinx.serialization.Serializable

@Serializable
data class User (
    var userId: String,
    var username: String,
    var password: String,
    var emailAddress: String,
    var imageURL: String,
    var createdDate: String,
)