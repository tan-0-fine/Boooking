package com.example.travelapp.ui.theme.api

import com.google.gson.annotations.SerializedName

// 1. Cấu trúc tổng trả về từ API
data class FlightResponse(
    val search_metadata: SearchMetadata?,
    val best_flights: List<FlightItem>?, // Vé tốt nhất
    val other_flights: List<FlightItem>? // Các vé khác
)

data class FlightSearchMetadata(
    val status: String
)

// 2. Một vé máy bay (Một hành trình)
data class FlightItem(
    val flights: List<FlightSegment>?, // Các chặng bay (thường chỉ có 1 nếu bay thẳng)
    val total_duration: Int?, // Tổng thời gian (phút)
    val price: Int? // Giá tiền (số nguyên)
)

// 3. Chi tiết một chặng bay (Segment)
data class FlightSegment(
    val departure_airport: AirportInfo?,
    val arrival_airport: AirportInfo?,
    val duration: Int?,
    val airplane: String?,
    val airline: String?,
    val airline_logo: String?,
    val flight_number: String?
)

// 4. Thông tin sân bay
data class AirportInfo(
    val name: String?,
    val id: String?, // Ví dụ: SGN, HAN
    val time: String? // Giờ bay: "2024-03-15 07:00"
)