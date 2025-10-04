package com.example.carggyappassignment.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carggyappassignment.ui.theme.Orange

@Composable
fun CustomButton(
    onClick: () -> Unit,
    buttonText:String,
    isLoading: Boolean = false,
){
    Button(
        onClick = if(!isLoading) onClick else ({}),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Orange),
        enabled = !isLoading
    ) {
        if(isLoading){
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }else{
            Text(
                text = buttonText,
                fontSize = 20.sp
            )
        }
    }
}