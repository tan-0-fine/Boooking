package com.example.travelapp.ui.theme.data

// Giữ nguyên Enum
enum class TripStatus(val label: String) {
    ACTIVE("Đang hoạt động"),
    PAST("Đã qua"),
    CANCELLED("Đã hủy")
}

data class Trip(
    val id: String = "",
    val title: String = "",        // Tên hiển thị chính (VD: Tên KS, Tên hãng bay)
    val location: String = "",     // Địa chỉ hoặc Sân bay
    val date: String = "",         // Ngày đi - về dạng chuỗi hiển thị
    val price: Long = 0,           // Giá tiền (Số Long)
    val imageRes: Int = 0,         // Ảnh nội bộ (nếu có)
    val type: String = "HOTEL",    // "HOTEL" hoặc "FLIGHT"
    val status: TripStatus = TripStatus.ACTIVE,

    // Các trường chi tiết
    val roomName: String = "",     // Tên phòng hoặc Tên chuyến bay
    val roomImage: String = "",    // Link ảnh
    val checkIn: String = "",      // Giờ đi / Check-in
    val checkOut: String = "",     // Giờ đến / Check-out
    val guestCount: Int = 1,
    val paymentMethod: String = "",
    val seatNumber: String = ""    // Số ghế (quan trọng cho máy bay)
)