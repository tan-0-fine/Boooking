package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// 1. DATA MODEL VOUCHER
data class VoucherData(
    val code: String,
    val title: String,
    val description: String,
    val discountAmount: Int, // Giảm bao nhiêu tiền (VD: 50000)
    val expiryDate: String
)

@Composable
fun Voucher(navController: NavHostController) {
    // 2. DANH SÁCH VOUCHER GIẢ LẬP
    val vouchers = listOf(
        VoucherData("HE2024", "Chào Hè Sôi Động", "Giảm 10% tối đa 100k cho đơn từ 500k", 100000, "30 thg 06"),
        VoucherData("WELCOME", "Khách hàng mới", "Giảm ngay 50k cho lần đặt đầu tiên", 50000, "Vô thời hạn"),
        VoucherData("VIPMEMBER", "Tri ân khách VIP", "Giảm 200k cho khách sạn 5 sao", 200000, "15 thg 08"),
        VoucherData("APPONLY", "Đặt trên App", "Giảm 20k phí dịch vụ", 20000, "31 thg 12")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Màu nền xám nhẹ cho nổi voucher
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 40.dp, bottom = 16.dp, start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(
                text = "Mã giảm giá",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // --- DANH SÁCH VOUCHER ---
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vouchers) { voucher ->
                VoucherItem(voucher = voucher, onApply = {
                    navController.navigate("payment_screen") {
                        popUpTo("payment_screen") { inclusive = true }
                    }
                })
            }
        }
    }
}

// 3. UI CHO TỪNG VÉ (Design giống vé xem phim)
@Composable
fun VoucherItem(voucher: VoucherData, onApply: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().height(110.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // PHẦN TRÁI (Màu xanh, chứa icon hoặc tên ngắn)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF00BFFF)), // Màu xanh chủ đạo
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("VOUCHER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${voucher.discountAmount / 1000}k",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }
            }

            // PHẦN PHẢI (Thông tin chi tiết)
            Column(
                modifier = Modifier
                    .weight(2.5f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(voucher.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(voucher.description, fontSize = 12.sp, color = Color.Gray, maxLines = 2)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("HSD: ${voucher.expiryDate}", fontSize = 11.sp, color = Color(0xFFFF9800))

                }
            }
        }
    }
}
