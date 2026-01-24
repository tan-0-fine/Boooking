package com.example.travelapp.ui.theme.screen

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
import androidx.navigation.NavHostController
import com.example.travelapp.R
import com.example.travelapp.ui.theme.component.EmptyState
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel


@Composable
fun Favorite(
    navController: NavHostController,
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val favorites = favoriteViewModel.favorites
    val favoriteViewModel: FavoriteViewModel = viewModel()


    if (favorites.isEmpty()) {
        EmptyState("Bạn chưa lưu mục nào cả!")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favorites) { hotel ->
                HotelCardVertical(
                    hotel = hotel,
                    onClick = {
                        navController.navigate("hotel_detail")
                    }
                )
            }
        }
    }
}

