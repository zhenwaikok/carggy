package com.example.carggyappassignment.widgets

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.ui.graphics.Color
import com.example.carggyappassignment.ui.theme.Orange
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceLogDatePicker(
    selectedDate: MutableLongState,
    onDismissRequest: () -> Unit,
    onYesClick: () -> Unit
){
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = Date().time
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                dateState.selectedDateMillis?.let {
                    selectedDate.longValue = it
                }

                onYesClick()
            }) {
                Text(
                    text = "Yes",
                    color = Orange
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = "No",
                    color = Orange
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        )
    ) {
        DatePicker(
            state = dateState
        )
    }
}