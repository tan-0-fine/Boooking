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
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.request.ImageRequest
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel

@Composable
fun HotelCard(
    hotel: HotelProperty,
    isFavorite: Boolean, // <--- Thêm dòng này: Trạng thái tim (đỏ hay trắng)
    onFavoriteClick: (HotelProperty) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {

            Box {
                AsyncImage(
                    model = hotel.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(130.dp)
                        .fillMaxWidth()
                        .clickable { onClick() }
                )

                IconButton(
                    onClick = {
                        onFavoriteClick(hotel)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .zIndex(1f) // 🔥 BẮT BUỘC
                        .background(Color.White, CircleShape)

                ) {
                    Icon(
                        imageVector =
                            if (isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                        tint = if (isFavorite) Color.Red else Color.Black,
                        contentDescription = "Favorite"
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

