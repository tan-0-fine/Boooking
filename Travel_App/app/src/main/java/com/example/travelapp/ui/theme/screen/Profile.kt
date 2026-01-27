package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // Lấy user hiện tại
    val currentUser = auth.currentUser

    // 1. XỬ LÝ DỮ LIỆU HIỂN THỊ
    // Nếu tên chưa có (null) thì hiện "Khách hàng"
    val userName = if (currentUser?.displayName.isNullOrEmpty()) "Khách hàng" else currentUser?.displayName!!
    val userEmail = currentUser?.email ?: "Chưa cập nhật email"

    // 2. TẠO CHỮ CÁI AVATAR (Lấy chữ cái đầu của tên, viết hoa)
    val avatarLetter = if (userName.isNotEmpty()) userName.first().toString().uppercase() else "A"

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {

        // --- PHẦN HEADER (XANH DƯƠNG) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF00B6F0))
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar tròn
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarLetter, // Đã có biến này, hết lỗi đỏ
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00B6F0)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Tên & Email
                Column {
                    Text(userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(userEmail, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))

                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Thành viên", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // --- DANH SÁCH TÙY CHỌN ---
        Column(modifier = Modifier.padding(16.dp)) {

            // Mục 1: Tài khoản -> Bấm vào chuyển sang EditProfile
            ProfileOptionItem(
                icon = Icons.Default.Person,
                title = "Thông tin cá nhân",
                subtitle = "Cập nhật tên, số điện thoại"
            ) {
                // 3. GỌI LỆNH CHUYỂN TRANG Ở ĐÂY
                navController.navigate(Screen.EditProfile.route)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mục 2: Hỗ trợ
            ProfileOptionItem(icon = Icons.Default.Call, title = "Hỗ trợ khách hàng", subtitle = "Liên hệ chúng tôi 24/7") {}

            Spacer(modifier = Modifier.height(12.dp))

            // Mục 3: Cài đặt
            ProfileOptionItem(icon = Icons.Default.Settings, title = "Cài đặt ứng dụng", subtitle = "Ngôn ngữ, thông báo") {}

            Spacer(modifier = Modifier.height(30.dp))

            // NÚT ĐĂNG XUẤT
            Button(
                onClick = {
                    auth.signOut() // Đăng xuất Firebase
                    // Quay về màn hình đăng nhập, xóa hết lịch sử back để không bấm lùi lại được
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Đăng xuất", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileOptionItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF00B6F0), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
    }
}
