package com.example.travelapp.ui.theme.viewmodel

// File: FavoriteViewModel.kt
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.travelapp.ui.theme.api.HotelProperty
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Lưu ý: Sửa kế thừa thành AndroidViewModel(application)
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("my_travel_app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Biến chứa danh sách yêu thích
    private val _favoriteHotels = MutableStateFlow<List<HotelProperty>>(emptyList())
    val favoriteHotels: StateFlow<List<HotelProperty>> = _favoriteHotels.asStateFlow()

    init {
        loadFavorites() // Load lại danh sách khi mở app
    }

    // Hàm xử lý khi ấn nút tim
    fun toggleFavorite(hotel: HotelProperty) {
        val currentList = _favoriteHotels.value.toMutableList()

        // Kiểm tra xem khách sạn đã có trong list chưa (so sánh theo ID hoặc object)
        val existingIndex = currentList.indexOfFirst { it.name == hotel.name } // Tốt nhất nên so sánh bằng ID: it.id == hotel.id

        if (existingIndex != -1) {
            // Nếu có rồi -> Xóa đi (Bỏ tim)
            currentList.removeAt(existingIndex)
        } else {
            // Nếu chưa có -> Thêm vào (Thả tim)
            currentList.add(hotel)
        }

        // Cập nhật lên UI và Lưu vào máy
        _favoriteHotels.value = currentList
        saveFavorites(currentList)
    }

    // Lưu danh sách vào máy (chuyển thành chuỗi JSON)
    private fun saveFavorites(list: List<HotelProperty>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString("favorite_list_key", json).apply()
    }

    // Đọc danh sách từ máy ra
    private fun loadFavorites() {
        val json = sharedPreferences.getString("favorite_list_key", null)
        if (json != null) {
            val type = object : TypeToken<List<HotelProperty>>() {}.type
            _favoriteHotels.value = gson.fromJson(json, type)
        }
    }
}