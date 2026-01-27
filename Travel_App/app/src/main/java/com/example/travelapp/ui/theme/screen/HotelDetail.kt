HotelDetail.kt
package com.example.travelapp.ui.theme.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
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
import com.example.travelapp.ui.theme.api.HotelProperty
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.navigation.Screen
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetail(
    navController: NavHostController,
    hotel: HotelProperty,
    viewModel: FavoriteViewModel // Chỉ cần viewModel là đủ
) {
    // 1. STATE QUẢN LÝ UI
    var isExpanded by remember { mutableStateOf(false) }

    // 2. LOGIC YÊU THÍCH (DataStore)
    // Lấy toàn bộ danh sách yêu thích
    val favoriteList by viewModel.favoriteHotels.collectAsState()

    // --- THÊM DÒNG NÀY ĐỂ TÍNH TOÁN TRẠNG THÁI ---
    // Kiểm tra xem khách sạn hiện tại (hotel.id) có nằm trong danh sách không
    val isFavorite = favoriteList.any { it.name == hotel.name }
    // ---------------------------------------------

    // 3. XỬ LÝ NGÀY THÁNG
    val displayFormatter = SimpleDateFormat("dd 'thg' MM", Locale("vi", "VN"))
    var checkInDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var checkOutDate by remember { mutableStateOf(System.currentTimeMillis() + 86400000) }

    val checkInString = displayFormatter.format(Date(checkInDate))
    val checkOutString = displayFormatter.format(Date(checkOutDate))

    // 4. DATE PICKER DIALOGS
    var showCheckInPicker by remember { mutableStateOf(false) }
    var showCheckOutPicker by remember { mutableStateOf(false) }

    if (showCheckInPicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = checkInDate)
        DatePickerDialog(
            onDismissRequest = { showCheckInPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { checkInDate = it }
                    showCheckInPicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showCheckOutPicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = checkOutDate)
        DatePickerDialog(
            onDismissRequest = { showCheckOutPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { checkOutDate = it }
                    showCheckOutPicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // 5. GIAO DIỆN CHÍNH
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // --- PHẦN NỘI DUNG CUỘN ĐƯỢC ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            // A. HEADER (ẢNH + NÚT BACK + NÚT TIM)
            Box {
                AsyncImage(
                    model = hotel.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                    contentScale = ContentScale.Crop
                )

                // Nút Back
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 40.dp, start = 16.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }

                // --- NÚT TIM (Góc phải trên) ---
                IconButton(
                    onClick = { viewModel.toggleFavorite(hotel) }, // Gọi hàm trong ViewModel
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 40.dp, end = 16.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        // Dùng biến isFavorite vừa tính ở trên
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {

                // B. KHUNG CHỌN NGÀY
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.clickable { showCheckInPicker = true }) {
                        Text("Nhận phòng", fontSize = 12.sp, color = Color.Gray)
                        Text(checkInString, color = Color(0xFF2F80ED), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Color.LightGray)

                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.clickable { showCheckOutPicker = true }) {
                        Text("Trả phòng", fontSize = 12.sp, color = Color.Gray)
                        Text(checkOutString, color = Color(0xFF2F80ED), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // C. TÊN & RATING
                Text(
                    text = hotel.name ?: "Tên khách sạn",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB400), modifier = Modifier.size(18.dp))
                    Text(
                        text = " ${hotel.overall_rating ?: "N/A"} (${hotel.reviews ?: 0} Reviews)",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // D. MÔ TẢ
                Text(
                    text = hotel.description ?: "Mô tả đang cập nhật...",
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
                Text(
                    text = if (isExpanded) "Thu gọn" else "Xem thêm",
                    color = Color(0xFF2F80ED),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { isExpanded = !isExpanded }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // E. TIỆN ÍCH
                Text("Tiện ích nổi bật", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                val amenities = listOf("Hồ bơi vô cực", "WiFi tốc độ cao", "Spa & Massage", "Nhà hàng 5 sao", "Đưa đón sân bay", "Phòng Gym")
                amenities.chunked(2).forEach { pair ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        pair.forEach { item ->
                            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF27AE60))
                                Text(" $item", fontSize = 13.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        // --- 6. NÚT ĐẶT PHÒNG ---
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter),
            shadowElevation = 16.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Giá từ", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = hotel.price ?: "Liên hệ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F80ED)
                    )
                }

                Button(
                    onClick = {
                        AppData.currentHotel = hotel
                        AppData.checkInDate = checkInString   // Biến ngày bạn đã format (ví dụ "26/01/2026")
                        AppData.checkOutDate = checkOutString

                        // 2. Gọi lệnh chuyển màn hình đơn giản
                        navController.navigate(Screen.RoomSelection.route)
                    },
                    modifier = Modifier.height(50.dp).fillMaxWidth(0.6f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F80ED))
                ) {
                    Text("Xem phòng trống", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
