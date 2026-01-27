package com.example.travelapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.data.AppData.passengerInfo
import com.example.travelapp.ui.theme.data.Passengerinfo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerInfo(navController: NavHostController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Thông tin hành khách") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Hiển thị tóm tắt chuyến bay đang chọn
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Chuyến bay đi: ${AppData.currentFlight?.flights?.firstOrNull()?.departure_airport?.id} -> ${AppData.currentFlight?.flights?.firstOrNull()?.arrival_airport?.id}", fontWeight = FontWeight.Bold)
                    Text("Hãng: ${AppData.currentFlight?.flights?.firstOrNull()?.airline}", fontSize = 14.sp)
                }
            }

            Text("Nhập thông tin", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Họ (VD: Nguyen)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Tên đệm và Tên (VD: Van A)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                        Toast.makeText(context, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show()
                    } else {
                        // 1. Lưu thông tin vào AppData
                        AppData.passengerInfo = Passengerinfo(firstName, lastName, email, phone)

                        // 2. Chuyển sang chọn ghế
                        navController.navigate("seat_selection")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0044CC))
            ) {
                Text("Tiếp tục chọn chỗ ngồi", fontSize = 16.sp)
            }
        }
    }
}