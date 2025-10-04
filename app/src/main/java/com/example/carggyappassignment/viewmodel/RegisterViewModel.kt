package com.example.carggyappassignment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.carggyappassignment.models.User
import com.example.carggyappassignment.network.Repository
import com.example.carggyappassignment.network.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class RegisterViewModel:ViewModel() {
    private val repository = Repository()

    var isLoading by mutableStateOf(false)
        private set

    suspend fun register(
        username: String,
        password: String,
        emailAddress: String,
        imageURL: String,
    ): Boolean {
        return try{
            isLoading = true

            val firebaseSignUp = Firebase.auth.createUserWithEmailAndPassword(emailAddress, password).await()
            val userId = firebaseSignUp.user?.uid ?: return false

            val createdDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

            val user = User(
                userId = userId,
                username = username,
                password = password,
                emailAddress = emailAddress,
                imageURL = imageURL,
                createdDate = createdDate
            )

            when(repository.postUser(user = user)){
                is Resource.Success -> {
                    isLoading = false
                    true
                }

                is Resource.Error -> {
                    isLoading = false
                    false
                }
            }
        }catch (e: Exception){
            isLoading = false
            false
        }
    }
}