package com.example.carggyappassignment.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.carggyappassignment.ui.theme.Orange
import com.example.carggyappassignment.viewmodel.VehicleViewModel
import com.example.carggyappassignment.widgets.CustomButton
import com.example.carggyappassignment.widgets.CustomTextField
import com.example.carggyappassignment.widgets.LoadingIndicator
import com.example.carggyappassignment.widgets.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditVehicleScreen(
    navController: NavController,
    vehicleId: Int,
    vehicleVM: VehicleViewModel = viewModel()
) {
    val isLoading = vehicleVM.isLoading

    LaunchedEffect(vehicleId) {
        vehicleVM.getVehicleDetails(vehicleId = vehicleId)
    }

    val vehicleDetails = vehicleVM.vehicleDetails

    var vehicleType by remember { mutableStateOf("") }
    var vehicleName by remember { mutableStateOf("") }
    var vehicleBrand by remember { mutableStateOf("") }
    var plateNo by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(vehicleDetails) {
        vehicleDetails?.let {
            vehicleType = it.vehicleType ?: ""
            vehicleName = it.vehicleName ?: ""
            vehicleBrand = it.vehicleBrand ?: ""
            plateNo = it.plateNo ?: ""
            year = it.year ?: ""
        }
    }

    val imageURL = vehicleDetails?.vehicleImageURL

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "EDIT VEHICLE",
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
            Spacer(Modifier.height(20.dp))

            EditVehicleTextFields(
                vehicleType = vehicleType,
                onVehicleTypeChange = { vehicleType = it },
                vehicleName = vehicleName,
                onVehicleNameChange = { vehicleName = it },
                vehicleBrand = vehicleBrand,
                onVehicleBrandChange = { vehicleBrand = it },
                plateNo = plateNo,
                onPlateNoChange = { plateNo = it },
                year = year,
                onYearChange = { year = it },
                selectedImageUri = selectedImageUri,
                imageURL = imageURL,
                photoPickerLauncher = photoPickerLauncher,
                navController = navController,
                context = context,
                coroutineScope = coroutineScope,
                vehicleVM = vehicleVM,
                vehicleId = vehicleId,
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun EditVehicleTextFields(
    vehicleType: String,
    onVehicleTypeChange: (String) -> Unit,
    vehicleName: String,
    onVehicleNameChange: (String) -> Unit,
    vehicleBrand: String,
    onVehicleBrandChange: (String) -> Unit,
    plateNo: String,
    onPlateNoChange: (String) -> Unit,
    year: String,
    onYearChange: (String) -> Unit,
    selectedImageUri: Uri?,
    imageURL: String?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    navController: NavController,
    context: Context,
    coroutineScope: CoroutineScope,
    vehicleVM: VehicleViewModel,
    vehicleId: Int,
    isLoading: Boolean
) {
    Column {
        CustomTextField(
            textTitle = "Vehicle Type",
            inputValue = vehicleType,
            onChangeValue = onVehicleTypeChange,
            isPassword = false
        )

        Spacer(Modifier.height(20.dp))

        CustomTextField(
            textTitle = "Vehicle Name",
            inputValue = vehicleName,
            onChangeValue = onVehicleNameChange,
            isPassword = false
        )

        Spacer(Modifier.height(20.dp))

        CustomTextField(
            textTitle = "Vehicle Brand",
            inputValue = vehicleBrand,
            onChangeValue = onVehicleBrandChange,
            isPassword = false
        )

        Spacer(Modifier.height(20.dp))

        CustomTextField(
            textTitle = "Plate No",
            inputValue = plateNo,
            onChangeValue = onPlateNoChange,
            isPassword = false
        )

        Spacer(Modifier.height(20.dp))

        CustomTextField(
            textTitle = "Year",
            inputValue = year,
            onChangeValue = onYearChange,
            isPassword = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(20.dp))

        EditVehicleImagePicker(
            selectedImageUri = selectedImageUri,
            photoPickerLauncher = photoPickerLauncher,
            imageURL = imageURL
        )

        Spacer(Modifier.height(30.dp))

        CustomButton(
            isLoading = isLoading,
            onClick = {
                if (validateEditVehicleTextFields(
                        vehicleType = vehicleType,
                        vehicleName = vehicleName,
                        vehicleBrand = vehicleBrand,
                        plateNo = plateNo,
                        year = year,
                    ) { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                ) {
                    coroutineScope.launch {
                        val result = vehicleVM.editVehicle(
                            vehicleId = vehicleId,
                            vehicleType = vehicleType,
                            vehicleName = vehicleName,
                            vehicleBrand = vehicleBrand,
                            plateNo = plateNo,
                            year = year,
                            selectedImageUri = selectedImageUri
                        )

                        if (result) {
                            navController.navigateUp()
                            Toast.makeText(
                                context,
                                "Vehicle details updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to updated vehicle details, please try again!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            buttonText = "Save",
        )
    }
}

@Composable
fun EditVehicleImagePicker(
    selectedImageUri: Uri?,
    imageURL: String?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
) {
    Column {
        Text(
            text = "Vehicle Image",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "**Click on below to upload new picture.",
            fontSize = 16.sp,
        )

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                selectedImageUri != null -> {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                    )
                }

                !imageURL.isNullOrEmpty() -> {
                    AsyncImage(
                        model = imageURL,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

fun validateEditVehicleTextFields(
    vehicleType: String,
    vehicleName: String,
    vehicleBrand: String,
    plateNo: String,
    year: String,
    toastMessage: (String) -> Unit
): Boolean {
    if (vehicleType.isEmpty() || vehicleName.isEmpty()
        || vehicleBrand.isEmpty() || plateNo.isEmpty()
        || year.isEmpty()
    ) {
        toastMessage("All fields are required!")
        return false
    }

    return true
}