package com.example.travelapp.ui.theme.data

import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

// Đảm bảo Class này khớp với import ở RoomSelection
data class RoomOption(
    val id: String,
    val name: String,
    val description: String, // Ví dụ: "1 giường đôi lớn • 28 m²"
    val imageRes: String,    // Đường dẫn ảnh (URL hoặc Resource ID giả lập)
    val price: Long,         // Giá dạng số để tính toán
    val originalPrice: Long? = null, // Giá gốc (để gạch ngang)
    val tags: List<String> = emptyList() // Tiện ích: "Wifi miễn phí", "Hủy miễn phí"...
) {
    // Tự động tạo chuỗi hiển thị tiền (VD: "VND 1.850.000")
    val priceString: String
        get() = formatCurrency(price)

    val originalPriceString: String?
        get() = originalPrice?.let { formatCurrency(it) }

    private fun formatCurrency(amount: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return "VND ${formatter.format(amount)}"
    }
}

object RoomGenerator {

    // Hàm chính để tạo danh sách phòng
    fun generateRoomsForHotel(basePriceInput: String?): List<RoomOption> {
        // 1. Xử lý giá đầu vào: "$74" -> 74 (Long)
        var basePrice = parsePriceToLong(basePriceInput)

        // 2. Tự động đổi USD sang VND
        // Nếu giá < 100.000, hệ thống tự hiểu là USD và nhân với tỉ giá 25.450
        if (basePrice < 100000) {
            basePrice *= 25450
        }

        // Nếu vẫn bằng 0 (do lỗi parse), set giá mặc định để không bị lỗi UI
        if (basePrice == 0L) basePrice = 1500000

        return listOf(
            RoomOption(
                id = "standard",
                name = "Standard King Room",
                description = "1 giường đôi lớn\n28 m²\nHướng thành phố",
                imageRes = "https://example.com/room1.jpg",
                price = basePrice,
                tags = listOf("Wifi miễn phí", "Không hút thuốc", "Tiết kiệm 50%")
            ),
            RoomOption(
                id = "deluxe",
                name = "Deluxe Ocean View",
                description = "1 giường King\n35 m²\nBan công\nBồn tắm",
                imageRes = "https://example.com/room2.jpg",
                price = (basePrice * 1.4).toLong(), // Đắt hơn 40%
                originalPrice = (basePrice * 1.6).toLong(), // Giá ảo để gạch ngang
                tags = listOf("Bữa sáng miễn phí", "Hủy miễn phí", "Welcome Drink")
            ),
            RoomOption(
                id = "suite",
                name = "Executive Suite",
                description = "Phòng khách riêng\n60 m²\nTầng cao\nBồn sục Jacuzzi",
                imageRes = "https://example.com/room3.jpg",
                price = (basePrice * 2.5).toLong(), // Đắt gấp 2.5 lần
                tags = listOf("Quyền lợi Club Lounge", "Đưa đón sân bay", "Ăn uống phục vụ tại phòng")
            )
        )
    }

    // Hàm tiện ích: Chuyển chuỗi "$74" hoặc "VND 200.000" thành số
    private fun parsePriceToLong(priceString: String?): Long {
        if (priceString.isNullOrEmpty()) return 0L
        return try {
            // Xóa tất cả ký tự KHÔNG phải là số (xóa $, VND, dấu chấm, phẩy...)
            val numberOnly = priceString.replace("[^\\d]".toRegex(), "")
            numberOnly.toLong()
        } catch (e: Exception) {
            0L
        }
    }
}