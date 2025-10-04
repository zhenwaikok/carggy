package com.example.carggyappassignment.widgets

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(date:Long): String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(date)
}