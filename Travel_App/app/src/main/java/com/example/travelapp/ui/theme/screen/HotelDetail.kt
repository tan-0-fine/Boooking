package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.travelapp.ui.theme.api.HotelProperty
import com.example.travelapp.ui.theme.viewmodel.HomeViewModel
import com.example.travelapp.ui.theme.viewmodel.HotelDetailViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetail(
    navController: NavHostController,
    viewModel: HotelDetailViewModel = viewModel(),
    hotel: HotelProperty,
    isFavorite: Boolean, // <--- Thêm dòng này: Trạng thái tim (đỏ hay trắng)
    onFavoriteClick: (HotelProperty) -> Unit,
) {
    val hotel = viewModel.hotel ?: run {
        Text("No hotel selected")
        return
    }

    var checkIn by remember { mutableStateOf("Nhận phòng") }
    var checkOut by remember { mutableStateOf("Trả phòng") }

    var showCheckInPicker by remember { mutableStateOf(false) }
    var showCheckOutPicker by remember { mutableStateOf(false) }

    // ===== CHECK IN PICKER =====
    if (showCheckInPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showCheckInPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        checkIn = java.text.SimpleDateFormat(
                            "dd/MM/yyyy",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date(it))
                    }
                    showCheckInPicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    // ===== CHECK OUT PICKER =====
    if (showCheckOutPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showCheckOutPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        checkOut = java.text.SimpleDateFormat(
                            "dd/MM/yyyy",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date(it))
                    }
                    showCheckOutPicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        AsyncImage(
            model = hotel.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = checkIn,
                modifier = Modifier.clickable { showCheckInPicker = true }
            )
            Text(
                text = checkOut,
                modifier = Modifier.clickable { showCheckOutPicker = true }
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = hotel.name ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "⭐ ${hotel.overall_rating ?: "--"} (${hotel.reviews ?: 0} reviews)",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = hotel.price ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F80ED)
            )

            if (hotel.free_cancellation == true) {
                Text(
                    text = "Free cancellation",
                    color = Color(0xFF27AE60),
                    fontSize = 12.sp
                )
            }
        }
    }
}
