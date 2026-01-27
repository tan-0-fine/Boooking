package com.example.travelapp.ui.theme.api

// Tạo file mới: RoomOption.kt hoặc để chung file model
data class RoomOption(
    val id: String,
    val name: String, // Vd: Premier City View
    val price: String, // Vd: 4.280.000
    val originalPrice: String? = null, // Giá gốc (gạch ngang)
    val description: List<String>, // Giường, diện tích...
    val benefits: List<String>, // Hủy miễn phí, ăn sáng...
    val image: String // Link ảnh phòng
)