package com.example.travelapp.ui.theme.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.api.RetrofitClient
import com.example.travelapp.ui.theme.data.AppData
import kotlinx.coroutines.launch
import java.util.Calendar

// Data class đơn giản cho sân bay
data class AirportInfo(val code: String, val city: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightBookingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val apiService = RetrofitClient.instance

    // --- DANH SÁCH SÂN BAY MẪU ---
    val airportList = listOf(
        AirportInfo("SGN", "Hồ Chí Minh", "Tân Sơn Nhất"),
        AirportInfo("HAN", "Hà Nội", "Nội Bài"),
        AirportInfo("DAD", "Đà Nẵng", "Đà Nẵng"),
        AirportInfo("CXR", "Nha Trang", "Cam Ranh"),
        AirportInfo("PQC", "Phú Quốc", "Phú Quốc"),
        AirportInfo("DLI", "Đà Lạt", "Liên Khương"),
        AirportInfo("HPH", "Hải Phòng", "Cát Bi"),
        AirportInfo("VCA", "Cần Thơ", "Cần Thơ")
    )

    // --- STATE ---
    var fromAirport by remember { mutableStateOf(airportList.find { it.code == "SGN" }!!) }
    var toAirport by remember { mutableStateOf(airportList.find { it.code == "HAN" }!!) }

    // State Bottom Sheet (Để chọn sân bay)
    var showDepartureSheet by remember { mutableStateOf(false) }
    var showArrivalSheet by remember { mutableStateOf(false) }

    // State Date & Passenger
    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf("") }
    var showPassengerDialog by remember { mutableStateOf(false) }
    var adults by remember { mutableIntStateOf(1) }
    var children by remember { mutableIntStateOf(0) }
    var infants by remember { mutableIntStateOf(0) }
    var selectedClass by remember { mutableIntStateOf(1) }
    val classNames = mapOf(1 to "Phổ thông", 2 to "Phổ thông đặc biệt", 3 to "Thương gia", 4 to "Hạng nhất")
    var isLoading by remember { mutableStateOf(false) }

    // Date Picker Config
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val formattedMonth = String.format("%02d", month + 1)
            val formattedDay = String.format("%02d", dayOfMonth)
            selectedDate = "$year-$formattedMonth-$formattedDay"
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFCBEAF8))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Booking a Flight", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(30.dp))

            // 1. CHỌN SÂN BAY (Dạng Hộp chọn Dropdown)
            Text("Điểm khởi hành", fontSize = 12.sp, color = Color.Gray)
            FlightSelectorButton(
                code = fromAirport.code,
                city = fromAirport.city,
                icon = Icons.Default.FlightTakeoff,
                onClick = { showDepartureSheet = true }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text("Điểm đến", fontSize = 12.sp, color = Color.Gray)
            FlightSelectorButton(
                code = toAirport.code,
                city = toAirport.city,
                icon = Icons.Default.FlightLand,
                onClick = { showArrivalSheet = true }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. CHỌN NGÀY
            Text("Ngày khởi hành", fontSize = 12.sp, color = Color.Gray)
            OutlinedButton(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = if (selectedDate.isEmpty()) "Chọn ngày" else selectedDate, color = Color.Black)
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // 3. CHỌN HÀNH KHÁCH
            Text("Hành khách & Hạng vé", fontSize = 12.sp, color = Color.Gray)
            OutlinedButton(
                onClick = { showPassengerDialog = true },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("${adults + children + infants} Hành khách", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(classNames[selectedClass] ?: "Phổ thông", fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 4. NÚT SEARCH
            Button(
                onClick = {
                    if (selectedDate.isBlank()) {
                        Toast.makeText(context, "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isLoading = true
                    scope.launch {
                        try {
                            val response = apiService.searchFlights(
                                departureId = fromAirport.code,
                                arrivalId = toAirport.code,
                                date = selectedDate,
                                apiKey = "e5ca58b702415625737f21d18ee1b3c715d4023b43f23d606ec1949d2f715ee8",
                                adults = adults,
                                children = children,
                                travelClass = selectedClass
                            )
                            val flights = response.best_flights ?: response.other_flights ?: emptyList()

                            if (flights.isNotEmpty()) {
                                AppData.flightList = flights
                                AppData.passengerCount = adults + children
                                navController.navigate("flight_result")
                            } else {
                                Toast.makeText(context, "Không tìm thấy vé!", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6DA7B9)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if(isLoading) CircularProgressIndicator(color = Color.White) else Text("Tìm chuyến bay", fontSize = 18.sp)
            }
        }

        // --- BOTTOM SHEET CHỌN SÂN BAY ĐI ---
        if (showDepartureSheet) {
            AirportSelectionSheet(
                airports = airportList,
                onDismiss = { showDepartureSheet = false },
                onSelect = {
                    fromAirport = it
                    showDepartureSheet = false
                }
            )
        }

        // --- BOTTOM SHEET CHỌN SÂN BAY ĐẾN ---
        if (showArrivalSheet) {
            AirportSelectionSheet(
                airports = airportList,
                onDismiss = { showArrivalSheet = false },
                onSelect = {
                    toAirport = it
                    showArrivalSheet = false
                }
            )
        }

        // --- DIALOG CHỌN KHÁCH (Giữ nguyên) ---
        if (showPassengerDialog) {
            Dialog(onDismissRequest = { showPassengerDialog = false }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Chọn hạng vé", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        // (Giữ nguyên logic chọn hạng vé cũ của bạn...)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            ClassButton("Phổ thông", selectedClass == 1) { selectedClass = 1 }
                            ClassButton("PT đặc biệt", selectedClass == 2) { selectedClass = 2 }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            ClassButton("Thương gia", selectedClass == 3) { selectedClass = 3 }
                            ClassButton("Hạng nhất", selectedClass == 4) { selectedClass = 4 }
                        }
                        Divider(Modifier.padding(vertical = 16.dp))
                        CounterRow("Người lớn", ">12 tuổi", adults) { adults = it }
                        CounterRow("Trẻ em", "2-11 tuổi", children) { children = it }
                        CounterRow("Em bé", "<2 tuổi", infants) { infants = it }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { showPassengerDialog = false }, modifier = Modifier.fillMaxWidth()) { Text("Xác nhận") }
                    }
                }
            }
        }
    }
}

// ================= COMPONENT MỚI: NÚT CHỌN CÓ MŨI TÊN =================
@Composable
fun FlightSelectorButton(code: String, city: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF00B6F0))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(city, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(code, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Select", tint = Color.Gray)
        }
    }
}

// ================= COMPONENT MỚI: BOTTOM SHEET CHỌN SÂN BAY =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSelectionSheet(
    airports: List<AirportInfo>,
    onDismiss: () -> Unit,
    onSelect: (AirportInfo) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Chọn sân bay", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(airports) { airport ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(airport) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("${airport.city} (${airport.code})", fontWeight = FontWeight.Bold)
                            Text(airport.name, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// (Giữ nguyên ClassButton và CounterRow của bạn ở đây...)

@Composable
fun ClassButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFCBEAF8) else Color.White
        ),
        border = if(isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
        modifier = Modifier.width(140.dp)
    ) {
        Text(text, color = if (isSelected) Color(0xFF0044CC) else Color.Black, fontSize = 12.sp)
    }
}

@Composable
fun CounterRow(label: String, subLabel: String, count: Int, onCountChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(label, fontWeight = FontWeight.Bold)
            Text(subLabel, fontSize = 12.sp, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (count > 0) onCountChange(count - 1) }) {
                Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE91E63))
            }
            Text("$count", modifier = Modifier.padding(horizontal = 8.dp), fontSize = 18.sp)
            IconButton(onClick = { onCountChange(count + 1) }) {
                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
fun FlightInput(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}