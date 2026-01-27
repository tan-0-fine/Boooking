package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllHotels(navController: NavHostController) {
    // 1. Lấy dữ liệu gốc từ AppData
    val originalList = AppData.hotelList

    // 2. Biến lưu từ khóa tìm kiếm
    var searchQuery by remember { mutableStateOf("") }

    // 3. Logic lọc danh sách theo tên khách sạn
    val filteredList = if (searchQuery.isEmpty()) {
        originalList
    } else {
        originalList.filter { hotel ->
            hotel.name?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tất cả khách sạn", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // --- THANH TÌM KIẾM ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm tên khách sạn...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00B6F0),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            // --- DANH SÁCH KHÁCH SẠN ---
            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if(originalList.isEmpty()) "Đang tải dữ liệu..." else "Không tìm thấy khách sạn nào")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { hotel ->
                        HotelCardVertical(hotel = hotel) {
                            // SỰ KIỆN CLICK: Lưu hotel vào AppData và chuyển trang
                            AppData.currentHotel = hotel
                            navController.navigate(Screen.HotelDetail.route)
                        }
                    }
                }
            }
        }
    }
}