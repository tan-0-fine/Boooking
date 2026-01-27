package com.example.travelapp.data // Hoặc package bạn đang dùng

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.travelapp.ui.theme.data.Trip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Tạo DataStore (Singleton)
val Context.dataStore by preferencesDataStore(name = "trip_settings")

class TripStorage(private val context: Context) {
    private val TRIPS_KEY = stringPreferencesKey("saved_trips")
    private val gson = Gson()

    // 1. Hàm lưu chuyến đi mới
    suspend fun addTrip(newTrip: Trip) {
        context.dataStore.edit { preferences ->
            // Lấy danh sách cũ ra trước
            val currentJson = preferences[TRIPS_KEY] ?: "[]"
            val type = object : TypeToken<List<Trip>>() {}.type
            val currentList: MutableList<Trip> = gson.fromJson(currentJson, type) ?: mutableListOf()

            // Thêm chuyến mới vào đầu danh sách
            currentList.add(0, newTrip)

            // Lưu ngược lại thành chuỗi JSON
            preferences[TRIPS_KEY] = gson.toJson(currentList)
        }
    }

    // 2. Hàm đọc danh sách chuyến đi (Trả về Flow để UI tự cập nhật)
    val getTrips: Flow<List<Trip>> = context.dataStore.data.map { preferences ->
        val json = preferences[TRIPS_KEY] ?: "[]"
        val type = object : TypeToken<List<Trip>>() {}.type
        try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 3. (Tuỳ chọn) Hàm xóa dữ liệu nếu muốn test lại từ đầu
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}

