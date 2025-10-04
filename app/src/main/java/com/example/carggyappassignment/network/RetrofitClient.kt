package com.example.carggyappassignment.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

object RetrofitClient {
    private const val BASE_URL = "http://carggyapi-dev.us-east-1.elasticbeanstalk.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

object ApiClient{
    val apiService: ApiService by lazy{
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}