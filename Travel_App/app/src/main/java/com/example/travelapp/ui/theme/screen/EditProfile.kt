package com.example.travelapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()

    // Các biến lưu dữ liệu nhập vào
    var name by remember { mutableStateOf(user?.displayName ?: "") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // 1. TỰ ĐỘNG LẤY DỮ LIỆU CŨ TỪ FIREBASE KHI MỞ MÀN HÌNH
    LaunchedEffect(key1 = user) {
        if (user != null) {
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Lấy sđt và địa chỉ cũ nếu có
                        phone = document.getString("phone") ?: ""
                        address = document.getString("address") ?: ""

                        // Nếu trong Auth chưa có tên thì lấy từ Firestore
                        if (name.isEmpty()) {
                            name = document.getString("name") ?: ""
                        }
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cập nhật thông tin", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00B6F0), // Màu xanh giống ảnh
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ô nhập Tên
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Họ và tên") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Ô nhập Số điện thoại
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Số điện thoại") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Ô nhập Địa chỉ
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Địa chỉ") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // NÚT LƯU THAY ĐỔI
            Button(
                onClick = {
                    // Kiểm tra xem user có thực sự tồn tại không
                    if (user == null) {
                        Toast.makeText(context, "Lỗi: Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show()
                    } else {
                        isLoading = true

                        // 1. Cập nhật tên hiển thị (Auth)
                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }

                        user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // 2. Chuẩn bị dữ liệu lưu Firestore
                                val userData = hashMapOf(
                                    "name" to name,
                                    "phone" to phone,
                                    "address" to address,
                                    "email" to user.email
                                )

                                // Ghi vào Firestore
                                firestore.collection("users").document(user.uid)
                                    .set(userData, SetOptions.merge())
                                    .addOnSuccessListener {
                                        isLoading = false
                                        Toast.makeText(context, "✅ Đã lưu thành công!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        // QUAN TRỌNG: Hiện lỗi cụ thể ra màn hình
                                        Toast.makeText(context, "❌ Lỗi lưu Database: ${e.message}", Toast.LENGTH_LONG).show()
                                        // In lỗi ra Logcat để kiểm tra
                                        e.printStackTrace()
                                    }
                            } else {
                                isLoading = false
                                Toast.makeText(context, "❌ Lỗi cập nhật tên: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B6F0)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Lưu thay đổi", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
