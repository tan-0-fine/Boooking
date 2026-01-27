package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.data.AppData

@Composable
fun BookingSuccessScreen(navController: NavHostController) {
    // Lấy thông tin lần cuối để hiển thị trước khi xóa
    val flight = AppData.currentFlight
    val hotel = AppData.currentHotel
    val room = AppData.currentSelectedRoom

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    // --- QUAN TRỌNG: RESET DỮ LIỆU ĐỂ ĐẶT LẦN SAU ---
                    resetAppData()

                    // Quay về trang chủ và xóa hết lịch sử back stack
                    navController.navigate("home") {
                        popUpTo(0) // Xóa sạch stack cũ
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B6F0)), // PrimaryBlue
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Về trang chủ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // --- ICON CHECK THÀNH CÔNG ---
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE8F5E9), CircleShape), // Màu nền xanh nhạt
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color(0xFF4CAF50), // Màu xanh lá đậm
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Đặt chỗ thành công!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Cảm ơn bạn đã sử dụng dịch vụ.\nMã đơn hàng của bạn là #HD${(1000..9999).random()}",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- THẺ TÓM TẮT ĐƠN HÀNG ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Chi tiết đơn hàng", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 0.5.dp)

                    if (flight != null) {
                        SuccessRow("Chuyến bay", "${flight.flights?.firstOrNull()?.departure_airport?.id} ➝ ${flight.flights?.firstOrNull()?.arrival_airport?.id}")
                        SuccessRow("Hãng bay", flight.flights?.firstOrNull()?.airline ?: "Unknown")
                        SuccessRow("Ghế", AppData.selectedSeat)
                    }

                    if (hotel != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        SuccessRow("Khách sạn", hotel.name ?: "")
                        SuccessRow("Phòng", room?.name ?: "Tiêu chuẩn")
                        SuccessRow("Ngày đi", "${AppData.checkInDate} - ${AppData.checkOutDate}")
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Một email xác nhận đã được gửi đến hộp thư của bạn.",
                        fontSize = 12.sp,
                        color = Color(0xFF00B6F0),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessRow(label: String, value: String) {
    if (value.isNotBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.Gray, fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
        }
    }
}

// Hàm này để xóa dữ liệu cũ, tránh việc user đặt tiếp mà vẫn còn dính thông tin cũ
fun resetAppData() {
    AppData.currentFlight = null
    AppData.currentHotel = null
    AppData.currentSelectedRoom = null
    AppData.selectedSeat = ""
    AppData.isFlightSelected = false
    AppData.passengerInfo = null
    AppData.selectedSeat = ""
}