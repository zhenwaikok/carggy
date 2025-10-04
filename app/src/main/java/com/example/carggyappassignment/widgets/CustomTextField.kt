package com.example.carggyappassignment.widgets

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textTitle: String,
    inputValue: String,
    onChangeValue: (String) -> Unit,
    isPassword: Boolean,
    readOnly: Boolean? = false,
    trailingIcon: (@Composable (() -> Unit))? = null,
    onClick: (() -> Unit)? = null,
    enable: Boolean? = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        Text(
            text = textTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = inputValue,
            enabled = enable ?: true,
            onValueChange = onChangeValue,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RectangleShape,
            readOnly = readOnly ?: false,
            trailingIcon = trailingIcon,
            interactionSource = interactionSource,
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    Log.d("CustomTextField", "CLICK TRIGGERED")
                    onClick?.invoke()
                },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black,
                cursorColor = Color.Black
            ),
            keyboardOptions = keyboardOptions
        )
    }
}

@Composable
fun ServiceDateField(
    dateText: String,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Service Date",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.CenterStart
        ) {
            OutlinedTextField(
                value = dateText,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape,
                readOnly = true,
                enabled = false,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Black,
                    disabledTextColor = Color.Black
                )
            )
        }
    }
}
