package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.travelapp.ui.theme.data.AppData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightResult(navController: NavHostController) {
    val flights = AppData.flightList

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Kết quả tìm kiếm") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (flights.isEmpty()) {
                Text("Không có chuyến bay nào", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(flights) { flight ->
                        val segment = flight.flights?.firstOrNull()

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Logo hãng bay
                                AsyncImage(
                                    model = segment?.airline_logo,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Fit
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = segment?.airline ?: "Unknown Airline", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = segment?.departure_airport?.time?.substring(11, 16) ?: "--:--", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text(" -> ", color = Color.Gray)
                                        Text(text = segment?.arrival_airport?.time?.substring(11, 16) ?: "--:--", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                    Text(text = "${flight.total_duration ?: 0} phút", fontSize = 12.sp, color = Color.Gray)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${flight.price ?: 0} đ",
                                        color = Color(0xFF00B6F0),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // NÚT CHỌN - ĐÃ BỎ DIALOG
                                    Button(
                                        onClick = {
                                            // 1. Lưu chuyến bay đã chọn
                                            AppData.currentFlight = flight
                                            AppData.paymentType = "FLIGHT" // Đánh dấu là trả tiền vé máy bay
                                            // 2. Chuyển thẳng sang màn hình thông tin hành khách
                                            navController.navigate("passenger_info")
                                        },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Text("Chọn")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}