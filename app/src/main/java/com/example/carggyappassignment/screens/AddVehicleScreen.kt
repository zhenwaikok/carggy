package com.example.carggyappassignment.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.carggyappassignment.widgets.TopBar
import kotlinx.coroutines.launch

@Composable
fun AddVehicleScreen(
    navController: NavController,
    vehicleVM: VehicleViewModel = viewModel()
) {
    var vehicleType by remember { mutableStateOf("") }
    var vehicleName by remember { mutableStateOf("") }
    var vehicleBrand by remember { mutableStateOf("") }
    var plateNo by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val isLoading = vehicleVM.isLoading

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "ADD NEW VEHICLE",
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

            AddNewVehicleTitle()

            Spacer(Modifier.height(20.dp))

            AddNewVehicleTextFields(
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
                photoPickerLauncher = photoPickerLauncher,
                isLoading = isLoading,
                onClick = {
                    if (validateVehicleTextFields(
                            vehicleType = vehicleType,
                            vehicleName = vehicleName,
                            vehicleBrand = vehicleBrand,
                            plateNo = plateNo,
                            year = year,
                            selectedImageUri = selectedImageUri
                        ) { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        coroutineScope.launch {
                            val result = vehicleVM.addNewVehicle(
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
                                    "Added vehicle successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to add vehicle, please try again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun AddNewVehicleTitle() {
    Column {
        Text(
            text = "Add New Vehicle",
            color = Orange,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Please enter your car details below to manage your vehicle easily.",
            fontSize = 18.sp,
        )
    }
}

@Composable
fun AddNewVehicleTextFields(
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
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    onClick: () -> Unit,
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

        VehicleImagePicker(
            selectedImageUri = selectedImageUri,
            photoPickerLauncher = photoPickerLauncher
        )

        Spacer(Modifier.height(30.dp))

        CustomButton(
            onClick = onClick,
            buttonText = "Add",
            isLoading = isLoading
        )
    }
}

@Composable
fun VehicleImagePicker(
    selectedImageUri: Uri?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
) {
    Column {
        Text(
            text = "Vehicle Image",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (selectedImageUri == null) {
                        Modifier.height(200.dp)
                    } else {
                        Modifier
                    }
                )
                .border(
                    width = if (selectedImageUri == null) 1.dp else 0.dp,
                    color = if (selectedImageUri == null) Color.Black else Color.Transparent
                )
                .clickable {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Upload,
                        contentDescription = "",
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Upload your picture here",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

fun validateVehicleTextFields(
    vehicleType: String,
    vehicleName: String,
    vehicleBrand: String,
    plateNo: String,
    year: String,
    selectedImageUri: Uri?,
    toastMessage: (String) -> Unit
): Boolean {
    if (vehicleType.isEmpty() || vehicleName.isEmpty()
        || vehicleBrand.isEmpty() || plateNo.isEmpty()
        || year.isEmpty() || selectedImageUri == null
    ) {
        toastMessage("All fields are required!")
        return false
    }

    return true
}