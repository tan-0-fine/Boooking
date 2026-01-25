package com.example.travelapp.ui.theme.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.travelapp.R
import com.example.travelapp.ui.theme.component.EmptyState
import com.example.travelapp.ui.theme.navigation.Screen
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel
import com.google.gson.Gson


// File: Favorite.kt
@Composable
fun Favorite(navController: NavController, favoriteViewModel: FavoriteViewModel) {
    val favoriteList by favoriteViewModel.favoriteHotels.collectAsState()

    if (favoriteList.isEmpty()) {
        Text("Chưa có khách sạn yêu thích nào")
    } else {
        LazyColumn {
            items(favoriteList) { hotel ->
                // Tái sử dụng HotelCard hoặc dùng layout khác
                HotelCard(
                    hotel = hotel,
                    isFavorite = true, // Ở màn hình này thì chắc chắn là đang thích rồi
                    onFavoriteClick = { favoriteViewModel.toggleFavorite(it) }, // Bấm lần nữa để bỏ thích
                    onClick = {
                        val hotelJson = Uri.encode(Gson().toJson(hotel))
                        navController.navigate("${Screen.HotelDetail.route}/$hotelJson")
                    }
                )
            }
        }
    }
}