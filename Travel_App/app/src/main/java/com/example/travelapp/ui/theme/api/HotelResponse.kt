package com.example.travelapp.ui.theme.api

data class HotelResponse(
    val ads: List<HotelProperty>?,
    val properties: List<HotelProperty>? // Thông thường kết quả tự nhiên nằm ở đây
)

data class HotelProperty(
    val name: String?,
    val thumbnail: String?,           // Link ảnh để dùng với Coil
    val overall_rating: Double?,      // Ví dụ: 4.7
    val reviews: Int?,                // Số lượng review
    val extracted_price: Long?,       // Giá trị số để tính toán: 7378000
    val price: String?,               // Giá hiển thị: "₫7,378,000"
    val amenities: List<String>?,      // Danh sách tiện ích: Spa, Pool...
    val free_cancellation: Boolean?   // Chính sách hủy
)