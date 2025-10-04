package com.example.carggyappassignment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class LogInViewModel:ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    suspend fun login(
        emailAddress: String,
        password: String
    ): Boolean{
        return try {
            isLoading = true

            val signIn = Firebase.auth.signInWithEmailAndPassword(
                emailAddress,password
            ).await()

            if(signIn.user != null){
                isLoading = false
            }

            signIn.user != null
        }catch (e: Exception){
            isLoading = false

            false
        }
    }
}