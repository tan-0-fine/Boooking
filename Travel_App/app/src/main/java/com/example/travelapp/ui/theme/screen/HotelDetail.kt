package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.travelapp.ui.theme.viewmodel.HotelDetailViewModel

@Composable
fun HotelDetail(
    navController: NavHostController,
    hotel: HotelProperty
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AsyncImage(
            model = hotel.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentScale = ContentScale.Crop
        )

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

