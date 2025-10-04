package com.example.carggyappassignment.screens

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.carggyappassignment.R
import com.example.carggyappassignment.ui.theme.Orange
import com.example.carggyappassignment.viewmodel.ProfileViewModel
import com.example.carggyappassignment.widgets.CustomButton
import com.example.carggyappassignment.widgets.CustomTextField
import com.example.carggyappassignment.widgets.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URI

@Composable
fun AccountDetailsScreen(
    navController: NavController,
    profileVM: ProfileViewModel = viewModel()
){
    val isLoading = profileVM.isLoading
    val userDetails = profileVM.userDetails

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri}
    )

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userDetails) {
        username = userDetails?.username ?: ""
        email = userDetails?.emailAddress ?: ""
        password = userDetails?.password ?: ""
    }

    Scaffold (
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "ACCOUNT DETAILS",
                containerColor = Orange,
                titleContentColor = Color.White,
                isNavigationIcon = true,
                navController = navController
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(40.dp))

            ProfilePicture(
                imageURL = userDetails?.imageURL ?: "",
                selectedImageUri = selectedImageUri,
                photoPickerLauncher = photoPickerLauncher
            )

            Spacer(Modifier.height(30.dp))

            AccountDetails(
                username = username,
                onUsernameChange = {username = it},
                email = email,
                onEmailChange = {email = it},
                password = password,
                onPasswordChange = {password = it},
                selectedImageUri = selectedImageUri,
                isLoading = isLoading,
                context = context,
                coroutineScope = coroutineScope,
                profileVM = profileVM,
                navController = navController
            )
        }
    }
}

@Composable
fun ProfilePicture(
    imageURL: String?,
    selectedImageUri: Uri?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            when{
                selectedImageUri != null -> {
                    ProfileAsyncImage(
                        model = selectedImageUri
                    )
                }

                !imageURL.isNullOrEmpty() -> {
                    ProfileAsyncImage(
                        model = imageURL
                    )
                }

                else -> {
                    ProfileAsyncImage(
                        model = R.drawable.profile_image
                    )
                }
            }

            IconButton(
                onClick = {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(
                        color = Color.LightGray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Filled.CameraAlt,
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileAsyncImage(
    model: Any
){
    AsyncImage(
        model = model,
        contentDescription = "",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape),
        placeholder = painterResource(R.drawable.profile_image),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun AccountDetails(
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    selectedImageUri: Uri?,
    isLoading: Boolean,
    context: Context,
    coroutineScope: CoroutineScope,
    profileVM: ProfileViewModel,
    navController: NavController
){
    Column {
        CustomTextField(
            textTitle = "Username",
            inputValue = username,
            onChangeValue = onUsernameChange,
            isPassword = false
        )

        Spacer(Modifier.height(30.dp))

        CustomTextField(
            enable = false,
            textTitle = "Email Address",
            inputValue = email,
            onChangeValue = onEmailChange,
            isPassword = false,
            readOnly = true
        )

        Spacer(Modifier.height(30.dp))

        CustomTextField(
            textTitle = "Password",
            inputValue = password,
            onChangeValue = onPasswordChange,
            isPassword = false
        )

        Spacer(Modifier.height(50.dp))

        CustomButton(
            onClick = {
                if(validateAccountDetailsTextFields(
                        username = username,
                        email = email,
                        password = password,
                    ){ message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                ){
                    coroutineScope.launch {
                        val result = profileVM.editAccountDetails(
                            username = username,
                            password = password,
                            emailAddress = email,
                            selectedImageUri = selectedImageUri
                        )

                        if(result){
                            navController.navigateUp()
                            Toast.makeText(context, "Account details updated successfully!", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, "Failed to update account details, please try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            buttonText = "Save",
            isLoading = isLoading
        )
    }
}

fun validateAccountDetailsTextFields(
    username: String,
    email: String,
    password: String,
    toastMessage: (String) -> Unit
): Boolean{
    if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
        toastMessage("All fields are required!")
        return false
    }
    else if(password.length < 6){
        toastMessage("Password should at least or more than 6 characters!")
        return false
    }

    return true
}

