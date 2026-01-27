package com.example.travelapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
// Import Data của bạn
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.data.Trip
import com.example.travelapp.ui.theme.data.TripStatus
import com.example.travelapp.ui.theme.screen.formatCurrency

// Màu sắc
private val PrimaryBlue = Color(0xFF00B6F0)
private val TextGray = Color.Gray

@Composable
fun Trip(navController: NavHostController) {
    // 1. LẤY DỮ LIỆU THẬT TỪ APPDATA (Nơi PaymentScreen đã lưu vào)
    // Dùng remember để khi màn hình load lại nó lấy danh sách mới nhất
    val realTrips = remember { AppData.tripList }

    // State quản lý Tab đang chọn (Mặc định là ACTIVE - Đang hoạt động)
    var selectedTab by remember { mutableStateOf(TripStatus.ACTIVE) }

    // 2. LOGIC LỌC: Chỉ hiện chuyến đi khớp với Tab đang chọn
    // .reversed() để chuyến mới nhất hiện lên đầu
    val filteredTrips = realTrips.filter { it.status == selectedTab }.reversed()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(PrimaryBlue).padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Chuyến đi của tôi", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row {
                        Icon(Icons.Outlined.HelpOutline, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            // --- TABS (Đang hoạt động / Đã qua / Đã hủy) ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TripStatus.values().forEach { status ->
                    StatusTab(
                        text = status.label,
                        isSelected = selectedTab == status,
                        onClick = { selectedTab = status }
                    )
                }
            }

            // --- DANH SÁCH CHUYẾN ĐI ---
            if (filteredTrips.isEmpty()) {
                // Giao diện khi trống
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Outlined.HelpOutline, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Chưa có chuyến đi nào ở mục này", color = Color.Gray)
                }
            } else {
                // Giao diện khi có dữ liệu
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredTrips) { trip ->
                        TripItemCard(trip)
                    }
                }
            }
        }
    }
}

// --- GIỮ NGUYÊN CÁC COMPONENT GIAO DIỆN ĐẸP CỦA BẠN ---

@Composable
fun StatusTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFFD6F4FF) else Color(0xFFF2F4F8),
        border = if (isSelected) BorderStroke(1.dp, PrimaryBlue) else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.Black else Color.Gray,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun TripItemCard(trip: Trip) {
    // Logic xác định loại vé
    val isFlight = trip.type == "FLIGHT"

    // Logic hiển thị ảnh: Nếu có ảnh thật thì dùng, nếu không thì dùng ảnh placeholder tùy loại
    val imageUrl = if (trip.roomImage.isNotEmpty()) trip.roomImage
    else if (isFlight) "https://img.freepik.com/free-vector/airplane-flying-cloudy-sky_1308-31610.jpg"
    else "https://via.placeholder.com/150"

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // 1. Ảnh (Bên trái)
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                contentScale = ContentScale.Crop
            )

            // 2. Thông tin (Bên phải)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                // Tên
                Text(
                    text = if (isFlight) "✈️ ${trip.roomName}" else trip.roomName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Ghế ngồi (nếu là máy bay)
                if (isFlight && trip.seatNumber.isNotEmpty()) {
                    Text(
                        text = "Ghế: ${trip.seatNumber}",
                        fontSize = 12.sp,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ngày tháng
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(
                            if (isFlight) android.R.drawable.ic_menu_send
                            else android.R.drawable.ic_menu_my_calendar
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    val dateText = if (isFlight && trip.checkOut.isEmpty()) trip.checkIn
                    else "${trip.checkIn} - ${trip.checkOut}"
                    Text(dateText, fontSize = 12.sp, color = TextGray)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Số khách
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${trip.guestCount} khách", fontSize = 12.sp, color = TextGray)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))

                // Giá tiền & Trạng thái
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(PrimaryBlue))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(trip.status.label, color = PrimaryBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Text(formatCurrency(trip.price), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.Black)
                }

                // Phương thức thanh toán
                if(trip.paymentMethod.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💳 ${trip.paymentMethod}",
                        fontSize = 11.sp,
                        color = TextGray,
                        modifier = Modifier.background(Color(0xFFF2F4F8), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
