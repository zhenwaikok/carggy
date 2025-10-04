package com.example.carggyappassignment.screens

import android.content.Context
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
import com.example.carggyappassignment.widgets.formatDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun AddServiceLogScreen(
    navController: NavController,
    vehicleId: Int,
    serviceLogVM: ServiceLogViewModel = viewModel()
){
    val isLoading = serviceLogVM.isLoading

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var serviceName by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri}
    )

    Scaffold (
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "ADD NEW LOG",
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

            AddNewLogTitle()

            Spacer(Modifier.height(20.dp))

            AddNewLogTextFields(
                serviceName = serviceName,
                onServiceNameChange = { serviceName = it },
                serviceDescription = serviceDescription,
                onServiceDescriptionChange = { serviceDescription = it },
                servicePrice = servicePrice,
                onServicePriceChange = { servicePrice = it },
                selectedImageUri = selectedImageUri,
                photoPickerLauncher = photoPickerLauncher,
                context = context,
                coroutineScope = coroutineScope,
                serviceLogVM = serviceLogVM,
                vehicleId = vehicleId,
                navController = navController,
                isLoading = isLoading,
            )
        }
    }
}

@Composable
fun AddNewLogTitle(){
    Column {
        Text(
            text = "Add New Log",
            color = Orange,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Please enter service log details below for your vehicle.",
            fontSize = 18.sp,
        )
    }
}

@Composable
fun AddNewLogTextFields(
    serviceName: String,
    onServiceNameChange: (String) -> Unit,
    serviceDescription: String,
    onServiceDescriptionChange: (String) -> Unit,
    servicePrice: String,
    onServicePriceChange: (String) -> Unit,
    selectedImageUri: Uri?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    context: Context,
    coroutineScope: CoroutineScope,
    serviceLogVM: ServiceLogViewModel,
    vehicleId: Int,
    navController: NavController,
    isLoading: Boolean,
){

    val serviceTypeOption = listOf("Minor", "Major", "Maintenance", "Check")
    var selectedOption by remember { mutableStateOf(serviceTypeOption[0]) }
    val openDatePicker = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableLongStateOf(Date().time) }

    Column {
        CustomTextField(
            textTitle = "Service Name",
            inputValue = serviceName,
            onChangeValue = onServiceNameChange,
            isPassword = false
        )

        Spacer(Modifier.height(20.dp))

        ServiceTypeDropdown(
            selectedOption = selectedOption,
            serviceTypeOption = serviceTypeOption,
            onOptionSelected = {selectedOption = it}
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
            dateText = formatDate(selectedDate.longValue),
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

        ServiceLogImagePicker(
            selectedImageUri = selectedImageUri,
            photoPickerLauncher = photoPickerLauncher
        )

        Spacer(Modifier.height(30.dp))

        CustomButton(
            onClick = {
                if(validateServiceLogTextFields(
                    serviceName = serviceName,
                    serviceType = selectedOption,
                    serviceDescription = serviceDescription,
                    serviceDate = formatDate(selectedDate.longValue),
                    servicePrice = servicePrice,
                    selectedImageUri = selectedImageUri,
                    ){ message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                ){
                    coroutineScope.launch {
                        val result = serviceLogVM.addServiceLog(
                            serviceName = serviceName,
                            serviceType = selectedOption,
                            serviceDescription = serviceDescription,
                            serviceDate = formatDate(selectedDate.longValue),
                            servicePrice = servicePrice,
                            selectedImageUri = selectedImageUri,
                            vehicleId = vehicleId
                        )

                        if(result){
                            navController.navigateUp()
                            Toast.makeText(
                                context,
                                "Added service log successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            Toast.makeText(
                                context,
                                "Failed to add service log, please try again!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            buttonText = "Add",
            isLoading = isLoading
        )
    }

    if(openDatePicker.value){
        ServiceLogDatePicker(
            selectedDate = selectedDate,
            onDismissRequest = {openDatePicker.value = false},
            onYesClick = {openDatePicker.value = false},
        )
    }
}

@Composable
fun ServiceLogImagePicker(
    selectedImageUri: Uri?,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
){
    Column {
        Text(
            text = "Receipt Image",
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
        ){
            if(selectedImageUri != null){
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
            }else{
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

fun validateServiceLogTextFields(
    serviceName: String,
    serviceType: String,
    serviceDescription: String,
    serviceDate: String,
    servicePrice: String,
    selectedImageUri: Uri?,
    toastMessage: (String) -> Unit
): Boolean {
    if (serviceName.isEmpty() || serviceType.isEmpty()
        || serviceDescription.isEmpty() || serviceDate.isEmpty()
        || servicePrice.isEmpty() || selectedImageUri == null
    ) {
        toastMessage("All fields are required!")
        return false
    }

    return true
}
