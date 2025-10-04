package com.example.carggyappassignment.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carggyappassignment.R
import com.example.carggyappassignment.navigation.Routes
import com.example.carggyappassignment.ui.theme.Orange
import com.example.carggyappassignment.viewmodel.RegisterViewModel
import com.example.carggyappassignment.widgets.CustomButton
import com.example.carggyappassignment.widgets.CustomTextField
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    registerVM: RegisterViewModel = viewModel()
){
    val context = LocalContext.current

    val isLoading = registerVM.isLoading

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold (containerColor = Color.White) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 0.dp)
                .verticalScroll(rememberScrollState())
        ){
            RegisterLogo()

            Spacer(modifier = Modifier.height(40.dp))

            RegisterTitle()

            Spacer(Modifier.height(40.dp))

            CustomTextField(
                textTitle = "Username",
                inputValue = username,
                onChangeValue = {username = it},
                isPassword = false
            )

            Spacer(Modifier.height(20.dp))

            CustomTextField(
                textTitle = "Email Address",
                inputValue = email,
                onChangeValue = {email = it},
                isPassword = false
            )

            Spacer(Modifier.height(20.dp))

            CustomTextField(
                textTitle = "Password",
                inputValue = password,
                onChangeValue = {password = it},
                isPassword = true
            )

            Spacer(Modifier.height(20.dp))

            CustomTextField(
                textTitle = "Confirm Password",
                inputValue = confirmPassword,
                onChangeValue = {confirmPassword = it},
                isPassword = true
            )

            Spacer(Modifier.height(40.dp))

            CustomButton(
                isLoading = isLoading,
                buttonText = "Register",
                onClick = {
                    if(validateTextFields(
                        username = username,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                        )
                        { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    ){
                        coroutineScope.launch {
                            val result = registerVM.register(
                                username = username,
                                password = password,
                                emailAddress = email,
                                imageURL = ""
                            )

                            if(result){
                                navController.navigate(Routes.Login.route){
                                    popUpTo(0)
                                    launchSingleTop = true
                                }
                                Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "Registration failed!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )


            Spacer(Modifier.height(10.dp))

            LoginText(navController)

        }
    }
}

@Composable
fun RegisterLogo(){
    Image(
        painter = painterResource(id = R.drawable.carggy_logo),
        contentDescription = "Carggy Logo",
        modifier = Modifier
            .width(400.dp)
            .height(200.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun RegisterTitle(){
    Column() {
        Text(
            text = "Create",
            fontSize = 40.sp,
            color = Orange,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = "New Account",
            fontSize = 40.sp,
            color = Orange,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun LoginText(navController: NavController){
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Already have an account? ",
                fontSize = 15.sp,
                color = Color.Black
            )

            BasicText(
                text = AnnotatedString("Log In"),
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Login.route)
                },
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Orange,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}

fun validateTextFields(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    toastMessage: (String) -> Unit
): Boolean{
    if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
        toastMessage("All fields are required!")
        return false
    }
    else if(password.length < 6){
        toastMessage("Password should at least or more than 6 characters!")
        return false
    }
    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        toastMessage("Invalid email address!")
        return false
    }else if(confirmPassword != password){
        toastMessage("Confirm password doesn't match with password!")
        return false
    }

    return true
}

