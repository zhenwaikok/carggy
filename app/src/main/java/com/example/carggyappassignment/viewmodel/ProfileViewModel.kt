package com.example.carggyappassignment.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carggyappassignment.models.User
import com.example.carggyappassignment.network.Repository
import com.example.carggyappassignment.network.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ProfileViewModel:ViewModel() {
    private val repository = Repository()

    var isLoading by mutableStateOf(false)
        private set

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val userId = currentUser?.uid

    var userDetails by mutableStateOf<User?>(null)
        private set


    init {
        viewModelScope.launch {
            getUserDetails()
        }
    }

    suspend fun getUserDetails(){
        try{
            isLoading = true

            when(val response = repository.getUserDetails(userId = userId ?: "")){
                is Resource.Success -> {
                    userDetails = response.data
                    isLoading = false
                }

                is Resource.Error -> {
                    userDetails = null
                    isLoading = false
                }
            }
        }catch (e:Exception){
            userDetails = null
            isLoading = false
        }
    }

    suspend fun editAccountDetails(
        username: String,
        password: String,
        emailAddress: String,
        selectedImageUri: Uri?,
    ): Boolean{
        return try{
            isLoading = true

            currentUser?.let { user ->
                val credential = EmailAuthProvider.getCredential(user.email!!, userDetails?.password ?: "")
                try {
                    user.reauthenticate(credential).await()
                } catch (e: Exception) {
                    isLoading = false
                    return false
                }

                if (password != userDetails?.password) {
                    try{
                        user.updatePassword(password).await()
                    }
                    catch (e:Exception){
                        isLoading = false
                        return false
                    }
                }
            }

            var profileImageUrl by mutableStateOf("")

            if(selectedImageUri == null){
                profileImageUrl = userDetails?.imageURL ?: ""
            }
            else{
                val imageName = "${UUID.randomUUID()}.jpg"
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("profileImages/$imageName")

                try {
                    imageRef.putFile(selectedImageUri).await()
                } catch (e: Exception) {
                    isLoading = false
                    return false
                }

                profileImageUrl = imageRef.downloadUrl.await().toString()
            }

            val user = User(
                userId = userId ?: "",
                username = username,
                password = password,
                emailAddress = emailAddress,
                imageURL = profileImageUrl,
                createdDate = userDetails?.createdDate ?: ""
            )

            when(repository.putUser(
                userId = userId ?: "",
                user = user
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
        }catch (e:Exception){
            isLoading = false
            false
        }
    }

    fun signOut():Boolean{
        return try {
            isLoading = true

            Firebase.auth.signOut()

            isLoading = false

            true
        }catch (e:Exception){
            isLoading = false
            false
        }
    }
}