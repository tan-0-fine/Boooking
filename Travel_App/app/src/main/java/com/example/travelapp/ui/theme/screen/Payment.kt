package com.example.travelapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.data.Trip
import com.example.travelapp.ui.theme.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

// --- MÀU SẮC ---
val PrimaryBlue = Color(0xFF00B6F0)
val GrayText = Color.Gray

// --- CẬP NHẬT ENUM PHƯƠNG THỨC THANH TOÁN ---
enum class PaymentMethod(val title: String, val icon: ImageVector, val color: Color) {
    MOMO("Ví MoMo", Icons.Default.Smartphone, Color(0xFFA50064)),       // Màu hồng MoMo
    ZALOPAY("ZaloPay", Icons.Default.AccountBalanceWallet, Color(0xFF0068FF)), // Xanh Zalo
    BANK("Chuyển khoản Ngân hàng", Icons.Default.AccountBalance, Color(0xFF1976D2)), // Xanh bank
    CREDIT_CARD("Thẻ tín dụng / Visa", Icons.Default.CreditCard, Color.Black),
    PAY_AT_HOTEL("Thanh toán tại khách sạn", Icons.Default.Home, Color(0xFFE65100))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavHostController) {
    val context = LocalContext.current

    // 1. LẤY DỮ LIỆU
    val hotel = AppData.currentHotel
    val room = AppData.currentSelectedRoom
    val checkIn = AppData.checkInDate
    val checkOut = AppData.checkOutDate
    val flight = AppData.currentFlight
    val passenger = AppData.passengerInfo
    val seat = AppData.selectedSeat
    val currentUser = FirebaseAuth.getInstance().currentUser

    // 2. LOGIC INPUT
    val initialName = if (passenger != null) "${passenger.lastName} ${passenger.firstName}" else currentUser?.displayName ?: ""
    val initialEmail = passenger?.email ?: currentUser?.email ?: ""
    val initialPhone = passenger?.phone ?: ""

    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phone by remember { mutableStateOf(initialPhone) }

    // State Voucher & Payment
    var voucherCode by remember { mutableStateOf("") }
    var discountAmount by remember { mutableDoubleStateOf(0.0) }
    var isVoucherApplied by remember { mutableStateOf(false) }
    var voucherMessage by remember { mutableStateOf("") }

    // Mặc định chọn MoMo
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.MOMO) }

    // 3. TÍNH TOÁN GIÁ
    val isHotelPayment = AppData.paymentType == "HOTEL"
    val isFlightPayment = AppData.paymentType == "FLIGHT"

    // Tính tiền khách sạn (Chỉ tính nếu đang ở luồng thanh toán HOTEL)
    val nights = if (isHotelPayment && hotel != null) calculateNights(checkIn, checkOut) else 0
    val roomPricePerNight = if (room != null) parsePrice(room.priceString) else 0.0
    val totalRoomPrice = if (isHotelPayment) (roomPricePerNight * nights) else 0.0
    val hotelTax = totalRoomPrice * 0.1

    // Tính tiền máy bay (Chỉ tính nếu đang ở luồng thanh toán FLIGHT)
    val flightPrice = if (isFlightPayment) (flight?.price?.toDouble() ?: 0.0) else 0.0

    // Tổng tiền cuối cùng dựa trên loại thanh toán
    val totalAmountBeforeDiscount = if (isFlightPayment) flightPrice else (totalRoomPrice + hotelTax)

    val finalPrice = if ((totalAmountBeforeDiscount - discountAmount) > 0)
        totalAmountBeforeDiscount - discountAmount
    else 0.0

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Thanh toán", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = Color.White) {
                Button(
                    onClick = {
                        if (name.isBlank() || phone.isBlank()) {
                            Toast.makeText(context, "Vui lòng nhập tên và SĐT", Toast.LENGTH_SHORT).show()
                        } else {
                            // --- LOGIC LƯU VÀO TRIP ---
                            // --- LOGIC LƯU VÀO TRIP ---
                            val newTrip = if (AppData.paymentType == "FLIGHT") {
                                val segment = flight?.flights?.firstOrNull()
                                Trip(
                                    id = System.currentTimeMillis().toString(),
                                    title = "Chuyến bay đi ${segment?.arrival_airport?.id}",
                                    location = segment?.arrival_airport?.id ?: "Unknown",
                                    date = segment?.departure_airport?.time?.substring(0, 10) ?: "",
                                    price = finalPrice.toLong(),
                                    imageRes = 0,
                                    type = "FLIGHT",
                                    roomName = segment?.airline ?: "Vietnam Airlines",
                                    roomImage = segment?.airline_logo ?: "",
                                    checkIn = segment?.departure_airport?.time?.substring(11, 16) ?: "",
                                    guestCount = AppData.passengerCount,
                                    paymentMethod = selectedPaymentMethod.name,
                                    seatNumber = seat // Lưu số ghế từ AppData.selectedSeat
                                )
                            } else {
                                // Trong Payment.kt, phần Else (Thanh toán khách sạn)
                                Trip(
                                    id = System.currentTimeMillis().toString(),
                                    title = hotel?.name ?: "Khách sạn",

                                    // Sửa dòng này: Thêm ?: "" để xử lý trường hợp location bị null
                                    location = hotel?.location ?: "Chưa rõ vị trí",

                                    date = "$checkIn - $checkOut",
                                    price = finalPrice.toLong(),

                                    // Sửa dòng này: Đảm bảo giá trị truyền vào là Int (không được null)
                                    imageRes = hotel?.imageRes ?: 0,

                                    type = "HOTEL",
                                    roomName = room?.name ?: "Phòng tiêu chuẩn",

                                    // thumbnail là link ảnh từ API của bạn, dùng nó thay cho imageRes nếu muốn hiện ảnh thật
                                    roomImage = hotel?.thumbnail ?: "",

                                    checkIn = checkIn ?: "",
                                    checkOut = checkOut ?: "",
                                    guestCount = AppData.passengerCount,
                                    paymentMethod = selectedPaymentMethod.name
                                )
                            }

                            AppData.tripList.add(newTrip)

                            // --- CHUYỂN HƯỚNG ---
                            navController.navigate("booking_success") {
                                popUpTo(Screen.Home.route) { inclusive = false }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Thanh Toán • ${formatCurrency(finalPrice)}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CARD VÉ MÁY BAY ---
            if (isFlightPayment && flight != null) {
                SectionCard(title = "Chi tiết chuyến bay") {
                    InfoRow("Hãng bay", flight.flights?.firstOrNull()?.airline ?: "Vietnam Airlines")
                    InfoRow("Chặng bay", "${flight.flights?.firstOrNull()?.departure_airport?.id} ➝ ${flight.flights?.firstOrNull()?.arrival_airport?.id}")
                    InfoRow("Ghế ngồi", if (!seat.isNullOrEmpty()) seat else "Chưa chọn")
                    InfoRow("Giá vé", formatCurrency(flightPrice))
                }
            }

            // --- CARD KHÁCH SẠN (Chỉ hiện khi thanh toán khách sạn) ---
            if (isHotelPayment && hotel != null) {
                SectionCard(title = "Chi tiết khách sạn") {
                    InfoRow("Khách sạn", hotel.name ?: "")
                    InfoRow("Loại phòng", room?.name ?: "")
                    InfoRow("Thời gian", "$nights đêm ($checkIn - $checkOut)")
                    InfoRow("Tiền phòng", formatCurrency(totalRoomPrice))
                    InfoRow("Thuế (10%)", formatCurrency(hotelTax))
                }
            }

            // --- THÔNG TIN LIÊN HỆ ---
            Text("Thông tin liên hệ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Họ tên") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth())

            // --- PHƯƠNG THỨC THANH TOÁN (MỚI) ---
            Text("Phương thức thanh toán", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    PaymentOptionRow(PaymentMethod.MOMO, selectedPaymentMethod) { selectedPaymentMethod = it }
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    PaymentOptionRow(PaymentMethod.ZALOPAY, selectedPaymentMethod) { selectedPaymentMethod = it }
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    PaymentOptionRow(PaymentMethod.BANK, selectedPaymentMethod) { selectedPaymentMethod = it }
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    PaymentOptionRow(PaymentMethod.CREDIT_CARD, selectedPaymentMethod) { selectedPaymentMethod = it }
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    PaymentOptionRow(PaymentMethod.PAY_AT_HOTEL, selectedPaymentMethod) { selectedPaymentMethod = it }
                }
            }

            // --- MÃ GIẢM GIÁ ---
            Text("Mã giảm giá", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = voucherCode,
                    onValueChange = { voucherCode = it.uppercase() },
                    label = { Text("Nhập mã (SALE10)") },
                    modifier = Modifier.weight(1f),
                    enabled = !isVoucherApplied
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (!isVoucherApplied) {
                            if (voucherCode == "SALE10") {
                                discountAmount = totalAmountBeforeDiscount * 0.1
                                isVoucherApplied = true
                                voucherMessage = "Đã giảm 10%"
                            } else {
                                voucherMessage = "Mã không đúng"
                                discountAmount = 0.0
                            }
                        } else {
                            isVoucherApplied = false
                            discountAmount = 0.0
                            voucherCode = ""
                            voucherMessage = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isVoucherApplied) Color.Red else PrimaryBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isVoucherApplied) "Hủy" else "Áp dụng")
                }
            }
            if(voucherMessage.isNotEmpty()) Text(voucherMessage, color = if(isVoucherApplied) Color(0xFF4CAF50) else Color.Red, fontSize = 12.sp)

            // --- TỔNG KẾT ---
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tổng cộng", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(formatCurrency(finalPrice), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFE91E63))
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// --- COMPONENTS PHỤ ---

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = GrayText, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black, textAlign = TextAlign.End)
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(bottom = 8.dp))
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), thickness = 0.5.dp, color = Color.LightGray)
            content()
        }
    }
}

@Composable
fun PaymentOptionRow(method: PaymentMethod, selectedMethod: PaymentMethod, onSelect: (PaymentMethod) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(method) }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = method.icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = method.color // Dùng màu riêng cho từng phương thức
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(method.title, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        RadioButton(
            selected = (method == selectedMethod),
            onClick = { onSelect(method) },
            colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
        )
    }
}

// --- LOGIC HELPER ---
fun formatCurrency(amount: Number): String {
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(amount)
}

fun calculateNights(start: String?, end: String?): Int {
    if (start.isNullOrEmpty() || end.isNullOrEmpty()) return 0
    return try {
        val formatter = if (start.contains("/")) DateTimeFormatter.ofPattern("dd/MM/yyyy") else DateTimeFormatter.ISO_LOCAL_DATE
        val d1 = LocalDate.parse(start, formatter)
        val d2 = LocalDate.parse(end, formatter)
        ChronoUnit.DAYS.between(d1, d2).toInt().coerceAtLeast(1)
    } catch (e: Exception) { 1 }
}

fun parsePrice(priceString: String?): Double {
    if (priceString.isNullOrEmpty()) return 0.0
    val cleanString = priceString.replace(Regex("[^0-9]"), "")
    return cleanString.toDoubleOrNull() ?: 0.0
}
