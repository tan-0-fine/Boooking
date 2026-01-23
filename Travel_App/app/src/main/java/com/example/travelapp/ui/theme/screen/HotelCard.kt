package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.travelapp.ui.theme.api.HotelProperty
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Trong file component/HotelCard.kt
@Composable
fun HotelCard(
    hotel: HotelProperty,
    onClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {

            Box {
                // Ảnh khách sạn
                AsyncImage(
                    model = hotel.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(130.dp)
                        .fillMaxWidth()
                )

                // ❤️ Favorite icon
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(
                            Color.White.copy(alpha = 0.9f),
                            shape = CircleShape
                        )
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector =
                            if (isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }

            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = hotel.name ?: "Unknown Hotel",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 14.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${hotel.overall_rating ?: "--"}",
                        fontSize = 12.sp
                    )
                    Text(
                        text = " (${hotel.reviews ?: 0})",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = hotel.price ?: "",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F80ED),
                    fontSize = 14.sp
                )

                if (hotel.free_cancellation == true) {
                    Text(
                        text = "Free cancellation",
                        fontSize = 10.sp,
                        color = Color(0xFF27AE60)
                    )
                }
            }
        }
    }
}
