package com.example.travelapp.ui.theme.data

import com.example.travelapp.R

enum class PaymentMethod(val title: String, val iconRes: Int) {
    // Lưu ý: Mình đang để tạm hình "ic_launcher_foreground" (hình con robot Android)
    // để code không bị lỗi đỏ. Bạn hãy thay bằng tên ảnh icon thật của bạn (ví dụ: R.drawable.ic_momo)

    PAY_AT_HOTEL("Thanh toán tại khách sạn", R.drawable.hotel),
    CREDIT_CARD("Thẻ tín dụng / Ghi nợ", R.drawable.card),
    BANK_TRANSFER("Chuyển khoản ngân hàng", R.drawable.bank),
    MOMO("Ví MoMo", R.drawable.momo),
    ZALOPAY("Ví ZaloPay", R.drawable.zalopay);
}
