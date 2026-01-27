package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.data.RoomGenerator
// XÓA dòng này (nếu có):
// import com.example.travelapp.ui.theme.api.RoomOption

// THÊM/GIỮ dòng này:
import com.example.travelapp.ui.theme.data.RoomOption
import com.example.travelapp.ui.theme.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomSelection(
    navController: NavHostController
    // ĐÃ XÓA HẾT CÁC THAM SỐ (hotelName, checkInDate...) ĐỂ TRÁNH LỖI CRASH
) {
    // 1. LẤY DỮ LIỆU TỪ APPDATA
    val hotel = AppData.currentHotel
    val checkInDate = AppData.checkInDate
    val checkOutDate = AppData.checkOutDate

    // Lấy giá từ object Hotel để tạo phòng (Giả sử HotelProperty có biến 'price')
    // Bạn cần đảm bảo currentHotel không bị null. Nếu null thì gán giá mặc định.
    val basePriceString = hotel?.price ?: "1.000.000 VND"

    val rooms = remember(basePriceString) {
        RoomGenerator.generateRoomsForHotel(basePriceString)
    }

    var selectedRoom by remember { mutableStateOf<RoomOption?>(null) }
    val totalPassengers = if (AppData.passengerCount > 0) AppData.passengerCount else 1
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00B6F0))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = hotel?.name ?: "Chọn phòng", // Lấy tên từ AppData
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = "$checkInDate - $checkOutDate",
                    color = Color.White,
                    modifier = Modifier.padding(start = 48.dp),
                    fontSize = 14.sp
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    if (selectedRoom != null) {
                        // Lưu phòng đã chọn vào AppData
                        AppData.currentSelectedRoom = selectedRoom
                        // Ngày check-in/out đã có trong AppData từ màn hình trước rồi, ko cần gán lại

                        // Nếu chưa chọn vé máy bay thì set false/null để tránh lỗi hiển thị bên Payment
                        AppData.isFlightSelected = false
                        AppData.paymentType = "HOTEL"
                        navController.navigate(Screen.Payment.route)
                    }
                },
                enabled = (selectedRoom != null),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F80ED),
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text("Đặt ngay", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(rooms) { room ->
                RoomItemCard(
                    room = room,
                    isSelected = (selectedRoom?.id == room.id),
                    onSelect = { selectedRoom = room }
                )
            }
        }
    }
}

@Composable
fun RoomItemCard(
    room: RoomOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF2F80ED) else Color.LightGray
    val borderSize = if (isSelected) 2.dp else 1.dp
    val buttonText = if (isSelected) "Đã chọn" else "Chọn"
    val buttonColor = if (isSelected) Color(0xFF2F80ED) else Color.White
    val buttonTextColor = if (isSelected) Color.White else Color(0xFF2F80ED)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(borderSize, borderColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Tên phòng + Ảnh nhỏ
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(room.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    room.description.lines().forEach { desc ->
                        if (desc.isNotBlank()) {
                            Text("• ${desc.trim()}", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                AsyncImage(
                    model = room.imageRes,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

            // Tiện ích (Màu xanh lá)
            room.tags.forEach { tag ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF27AE60), modifier = Modifier.size(14.dp))
                    Text(" $tag", color = Color(0xFF27AE60), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Tag giảm giá (nếu có)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Surface(color = Color(0xFF00E5FF), shape = RoundedCornerShape(4.dp)) {
                    Text("Tiết kiệm 60%", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(color = Color(0xFF00E5FF), shape = RoundedCornerShape(4.dp)) {
                    Text("Miễn phí taxi sân bay", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Giá và nút Chọn
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Giá cho 2 đêm", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        room.priceString,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Button(
                    onClick = onSelect,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(
                            0xFF2F80ED
                        ) else Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF2F80ED)),
                    modifier = Modifier.height(40.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (isSelected) "Đã chọn" else "Chọn",
                        color = if (isSelected) Color.White else Color(0xFF2F80ED)
                    )
                }
            }
        }
    }
}
