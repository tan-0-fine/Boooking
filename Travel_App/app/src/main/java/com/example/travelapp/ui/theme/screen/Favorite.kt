package com.example.travelapp.ui.theme.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.travelapp.ui.theme.navigation.Screen
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel
import com.google.gson.Gson
import com.example.travelapp.R
import com.example.travelapp.ui.theme.data.AppData
@Composable
fun Favorite(navController: NavHostController, viewModel: FavoriteViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Danh sách", "Thông báo")
    val favoriteList by viewModel.favoriteHotels.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // --- HEADER ---
        Text(
            text = "Yêu thích",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White,
            modifier = Modifier.fillMaxWidth().background(Color(0xFF00BFFF)).padding(top = 40.dp, bottom = 16.dp, start = 16.dp)
        )

        // --- TABS ---
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color(0xFF00BFFF),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[selectedTab]), color = Color(0xFF00BFFF))
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal, color = if (selectedTab == index) Color(0xFF00BFFF) else Color.Gray) }
                )
            }
        }

        // --- NỘI DUNG ---
        if (selectedTab == 0) {
            if (favoriteList.isEmpty()) {
                EmptyStateView()
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(items = favoriteList, key = { it.name ?: it.hashCode() }) { hotel -> // Thêm key để tối ưu render
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clickable {
                                    // --- SỬA LỖI CRASH Ở ĐÂY ---
                                    AppData.currentHotel = hotel
                                    // 2. Điều hướng sang màn hình chi tiết (Không cần truyền tham số nữa)
                                    navController.navigate(Screen.HotelDetail.route)
                                },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // KHUNG CHỨA ẢNH + NÚT TIM
                                Box(modifier = Modifier.width(110.dp).fillMaxHeight()) {
                                    AsyncImage(
                                        model = hotel.thumbnail,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    // --- NÚT TIM (Góc trên trái của ảnh) ---
                                    IconButton(
                                        onClick = { viewModel.toggleFavorite(hotel) }, // Bấm phát xoá luôn
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(24.dp)
                                            .background(Color.White.copy(alpha = 0.7f), CircleShape)
                                            .align(Alignment.TopStart)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Unlike",
                                            tint = Color.Red,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                // THÔNG TIN
                                Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
                                    Text(hotel.name ?: "Tên khách sạn", fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB400), modifier = Modifier.size(16.dp))
                                        Text(" ${hotel.overall_rating ?: "4.5"}", fontSize = 14.sp, color = Color.Gray)
                                    }
                                    Text(hotel.price ?: "Liên hệ", fontWeight = FontWeight.Bold, color = Color(0xFF2F80ED), fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Tab Thông báo
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Notifications, null, modifier = Modifier.size(60.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Bạn không có thông báo nào", fontSize = 16.sp, color = Color.Gray)
                }
            }
        }
    }
}
// Giữ lại view Empty State cũ của bạn cho Tab Danh Sách
@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.emty_plane),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Bạn chưa lưu mục nào cả!",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Nhấn vào ❤ để lưu lại những điểm đến",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}