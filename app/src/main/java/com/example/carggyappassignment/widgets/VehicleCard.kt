package com.example.carggyappassignment.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.carggyappassignment.R

@Composable
fun VehicleCard(
        imageURL: String,
        vehicleNo: String,
        vehicleName: String,
        onClick: () -> Unit,
        onEditClick: () -> Unit,
        onDeleteClick: () -> Unit,
    ){
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .pointerInput(Unit){
                detectTapGestures (
                    onLongPress = {
                        expanded.value = true
                    },
                    onTap = {onClick() }
                )
            }
    ){
        Column() {
            AsyncImage(
                model = imageURL,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(
                    R.drawable.placeholder_image,
                )
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = vehicleNo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = vehicleName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            if(expanded.value){
                BottomContextMenu(
                    expanded = expanded,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}


