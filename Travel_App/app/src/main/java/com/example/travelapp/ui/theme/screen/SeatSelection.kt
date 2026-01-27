package com.example.travelapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelection(navController: NavHostController) {
    val context = LocalContext.current

    // 1. LẤY SỐ LƯỢNG KHÁCH TỪ DATA (Nếu chưa set thì mặc định là 1)
    val totalPassengers = if (AppData.passengerCount > 0) AppData.passengerCount else 1

    // 2. LOGIC "FAKE" GHẾ ĐÃ ĐẶT
    val bookedSeats = remember {
        val randomSeats = mutableListOf<String>()
        val rows = 1..10
        val cols = listOf("A", "B", "C", "D", "E", "F")
        repeat(15) {
            val r = rows.random()
            val c = cols.random()
            randomSeats.add("$r$c")
        }
        randomSeats
    }

    // 3. DANH SÁCH GHẾ ĐANG CHỌN (Dùng mutableStateListOf thay vì String đơn)
    val selectedSeats = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Chọn chỗ ngồi", fontWeight = FontWeight.Bold)
                        // Hiển thị số lượng cần chọn
                        Text(
                            text = "Hành khách: $totalPassengers người",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        // Trong SeatSelection.kt -> bottomBar -> Button

        bottomBar = {
            Surface(shadowElevation = 10.dp) {
                val ticketPrice = AppData.currentFlight?.price?.toDouble() ?: 1000000.0
                val currentTotal = ticketPrice * selectedSeats.size

                Button(
                    onClick = {
                        if (selectedSeats.size < totalPassengers) {
                            Toast.makeText(context, "Vui lòng chọn đủ $totalPassengers ghế!", Toast.LENGTH_SHORT).show()
                        } else {
                            // 1. LƯU DANH SÁCH GHẾ (VD: "1A, 1B")
                            AppData.selectedSeat = selectedSeats.sorted().joinToString(", ")

                            // 2. QUAN TRỌNG: Đánh dấu đây là thanh toán MÁY BAY
                            // Điều này giúp PaymentScreen biết chỉ hiện thông tin máy bay
                            AppData.paymentType = "FLIGHT"

                            // 3. Điều hướng sang payment
                            navController.navigate(Screen.Payment.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    enabled = selectedSeats.size == totalPassengers,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSeats.size == totalPassengers) Color(0xFF00B6F0) else Color.Gray
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        if (selectedSeats.size < totalPassengers)
                            "Chọn thêm ${totalPassengers - selectedSeats.size} ghế"
                        else
                            "Xác nhận (${selectedSeats.sorted().joinToString(", ")})"
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Chú thích
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(color = Color.White, text = "Trống", hasBorder = true)
                LegendItem(color = Color.LightGray, text = "Đã đặt")
                LegendItem(color = Color(0xFF00B6F0), text = "Đang chọn")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Buồng lái", color = Color.Gray, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(10) { rowIndex ->
                    val rowNum = rowIndex + 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Logic chọn ghế: Truyền hàm xử lý click vào SeatItem
                        val onSeatClick: (String) -> Unit = { id ->
                            if (selectedSeats.contains(id)) {
                                selectedSeats.remove(id) // Bỏ chọn
                            } else {
                                if (selectedSeats.size < totalPassengers) {
                                    selectedSeats.add(id) // Chọn thêm
                                } else {
                                    Toast.makeText(context, "Bạn chỉ được chọn tối đa $totalPassengers ghế", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        // Bên trái
                        Row {
                            SeatItem("A", rowNum, bookedSeats, selectedSeats, onSeatClick)
                            Spacer(modifier = Modifier.width(4.dp))
                            SeatItem("B", rowNum, bookedSeats, selectedSeats, onSeatClick)
                            Spacer(modifier = Modifier.width(4.dp))
                            SeatItem("C", rowNum, bookedSeats, selectedSeats, onSeatClick)
                        }

                        Text(
                            text = "$rowNum",
                            modifier = Modifier.width(30.dp).align(Alignment.CenterVertically),
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        // Bên phải
                        Row {
                            SeatItem("D", rowNum, bookedSeats, selectedSeats, onSeatClick)
                            Spacer(modifier = Modifier.width(4.dp))
                            SeatItem("E", rowNum, bookedSeats, selectedSeats, onSeatClick)
                            Spacer(modifier = Modifier.width(4.dp))
                            SeatItem("F", rowNum, bookedSeats, selectedSeats, onSeatClick)
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }
    }
}

// --- CẬP NHẬT ITEM GHẾ ---
@Composable
fun SeatItem(
    col: String,
    row: Int,
    bookedSeats: List<String>,
    selectedSeats: List<String>, // Nhận vào List thay vì String
    onSelect: (String) -> Unit
) {
    val seatId = "$row$col"
    val isBooked = bookedSeats.contains(seatId)
    val isSelected = selectedSeats.contains(seatId) // Kiểm tra có trong list không

    val backgroundColor = when {
        isBooked -> Color.LightGray
        isSelected -> Color(0xFF00B6F0)
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, if (isSelected) Color.Transparent else Color.LightGray, RoundedCornerShape(8.dp))
            .clickable(enabled = !isBooked) {
                onSelect(seatId)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = col,
            color = if (isSelected || isBooked) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LegendItem(color: Color, text: String, hasBorder: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
                .border(if (hasBorder) 1.dp else 0.dp, Color.Gray, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp)
    }
}