package com.example.carggyappassignment.widgets

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomContextMenu(
    expanded: MutableState<Boolean>,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
){
    ModalBottomSheet(
        onDismissRequest = {expanded.value = false},
        containerColor = Color.White
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = "Edit"
                )
            },
            leadingContent = { Icon(Icons.Default.Edit, contentDescription = "") },
            modifier = Modifier.clickable {
                onEditClick()
                expanded.value = false
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.White
            )
        )

        ListItem(
            headlineContent = {
                Text(
                        text = "Remove",
                        color = Color.Red
                    )
                },
            leadingContent = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "",
                    tint = Color.Red
                )
             },
            modifier = Modifier.clickable {
                onDeleteClick()
                expanded.value = false
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.White
            )
        )
    }
}
