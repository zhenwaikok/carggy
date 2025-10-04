package com.example.carggyappassignment.screens

import android.content.Context
import android.net.Uri
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
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.carggyappassignment.viewmodel.ServiceLogViewModel
import com.example.carggyappassignment.widgets.CustomButton
import com.example.carggyappassignment.widgets.CustomTextField
import com.example.carggyappassignment.widgets.ServiceDateField
import com.example.carggyappassignment.widgets.ServiceLogDatePicker
import com.example.carggyappassignment.widgets.ServiceTypeDropdown
import com.example.carggyappassignment.widgets.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditServiceLogScreen(
    navController: NavController,
    serviceLogId: Int,
    serviceLogVM: ServiceLogViewModel = viewModel()
){
    val coroutineScope = rememberCoroutineScope()

    val isLoading = serviceLogVM.isLoading

    var serviceName by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }
    var serviceDate by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        serviceLogVM.getServiceLogDetails(serviceLogId = serviceLogId)
    }

    val serviceLogDetails = serviceLogVM.serviceLogDetails

    LaunchedEffect(serviceLogDetails) {
        serviceName = serviceLogDetails?.serviceName ?: "-"
        serviceType = serviceLogDetails?.serviceType ?: "-"
        serviceDescription = serviceLogDetails?.serviceDescription ?: "-"
        serviceDate = serviceLogDetails?.serviceDate ?: "-"
        servicePrice = serviceLogDetails?.servicePrice ?: "-"
    }

    val imageURL = serviceLogDetails?.serviceImageURL

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri}
    )

    val context = LocalContext.current

    Scaffold (
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "EDIT SERVICE LOG",
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

            EditLogTextFields(
                serviceLogId = serviceLogId,
                serviceName = serviceName,
                onServiceNameChange = {serviceName = it},
                serviceType = serviceType,
                onServiceTypeChange = {serviceType = it},
                serviceDescription = serviceDescription,
                onServiceDescriptionChange = {serviceDescription = it},
                serviceDate = serviceDate,
                servicePrice = servicePrice,
                onServicePriceChange = {servicePrice = it},
                selectedImageUri = selectedImageUri,
                imageURL = imageURL,
                photoPickerLauncher = photoPickerLauncher,
                isLoading = isLoading,
                context = context,
                coroutineScope = coroutineScope,
                serviceLogVM = serviceLogVM,
                vehicleId = serviceLogDetails?.vehicleId ?: 0,
                navController = navController
            )
        }
    }
}

@Composable
fun EditLogTextFields(
    serviceLogId: Int,
    serviceName: String,
    onServiceNameChange: (String) -> Unit,
    serviceType: String,
    onServiceTypeChange: (String) -> Unit,
    serviceDescription: String,
    onServiceDescriptionChange: (String) -> Unit,
    serviceDate: String,
    servicePrice: String,
    onServicePriceChange: (String) -> Unit,
    selectedImageUri: Uri?,
    imageURL: String?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    isLoading: Boolean,
    context: Context,
    coroutineScope: CoroutineScope,
    serviceLogVM: ServiceLogViewModel,
    vehicleId: Int,
    navController: NavController
){
    val serviceTypeOption = listOf("Minor", "Major", "Maintenance", "Check")
    val openDatePicker = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableLongStateOf(Date().time) }

    var serviceDateState by remember { mutableStateOf("") }

    LaunchedEffect(serviceDate) {
        serviceDateState = serviceDate
    }

    Column {
        CustomTextField(
            textTitle = "Service Name",
            inputValue = serviceName,
            onChangeValue = onServiceNameChange,
            isPassword = false
        )

        Spacer(Modifier.height(20.dp))

        ServiceTypeDropdown(
            selectedOption = serviceType,
            serviceTypeOption = serviceTypeOption,
            onOptionSelected = onServiceTypeChange
        )

        Spacer(Modifier.height(20.dp))

        CustomTextField(
            textTitle = "Service Description",
            inputValue = serviceDescription,
            onChangeValue = onServiceDescriptionChange,
            isPassword = false
        )

        Spacer(Modifier.height(20.dp))

        ServiceDateField(
          dateText = serviceDateState,
          onClick = {
              openDatePicker.value = true
            }
        )

        Spacer(Modifier.height(20.dp))

        CustomTextField(
            textTitle = "Service Price",
            inputValue = servicePrice,
            onChangeValue = onServicePriceChange,
            isPassword = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(20.dp))

        EditServiceLogImagePicker(
            selectedImageUri = selectedImageUri,
            imageURL = imageURL,
            photoPickerLauncher = photoPickerLauncher
        )

        Spacer(Modifier.height(30.dp))

        CustomButton(
            onClick = {
                if(validateServiceLogEditTextFields(
                        serviceName = serviceName,
                        serviceType = serviceType,
                        serviceDescription = serviceDescription,
                        serviceDate = serviceDate,
                        servicePrice = servicePrice,
                    ){ message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                ){
                    coroutineScope.launch {
                        val result = serviceLogVM.editServiceLog(
                            serviceLogId = serviceLogId,
                            serviceName = serviceName,
                            serviceType = serviceType,
                            serviceDescription = serviceDescription,
                            serviceDate = serviceDateState,
                            servicePrice = servicePrice,
                            selectedImageUri = selectedImageUri,
                            vehicleId = vehicleId
                        )

                        if(result){
                            navController.navigateUp()
                            Toast.makeText(context, "Service log details updated successfully!", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Failed to edit service log, please try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            buttonText = "Save",
            isLoading = isLoading
        )
    }

    if(openDatePicker.value){
        ServiceLogDatePicker(
            selectedDate = selectedDate,
            onDismissRequest = {
                openDatePicker.value = false
            },
            onYesClick = {
                openDatePicker.value = false

                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDate.longValue))
                serviceDateState = formattedDate
            }
        )
    }
}

@Composable
fun EditServiceLogImagePicker(
    selectedImageUri: Uri?,
    imageURL: String?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
){
    Column {
        Text(
            text = "Receipt Image",
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
        ){
            when{
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

fun validateServiceLogEditTextFields(
    serviceName: String,
    serviceType: String,
    serviceDescription: String,
    serviceDate: String,
    servicePrice: String,
    toastMessage: (String) -> Unit
): Boolean {
    if (serviceName.isEmpty() || serviceType.isEmpty()
        || serviceDescription.isEmpty() || serviceDate.isEmpty()
        || servicePrice.isEmpty()
    ) {
        toastMessage("All fields are required!")
        return false
    }

    return true
}