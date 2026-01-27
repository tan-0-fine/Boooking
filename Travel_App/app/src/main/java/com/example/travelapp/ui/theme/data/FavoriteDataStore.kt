package com.example.travelapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.travelapp.ui.theme.api.HotelProperty // <--- Import đúng cái này
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.favoriteDataStore by preferencesDataStore(name = "favorite_prefs")

class FavoriteStorage(private val context: Context) {
    private val FAVORITE_KEY = stringPreferencesKey("favorite_hotels")
    private val gson = Gson()

    // Đổi Hotel -> HotelProperty
    val getFavorites: Flow<List<HotelProperty>> = context.favoriteDataStore.data.map { preferences ->
        val json = preferences[FAVORITE_KEY] ?: "[]"
        val type = object : TypeToken<List<HotelProperty>>() {}.type
        try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Đổi Hotel -> HotelProperty
    suspend fun toggleFavorite(hotel: HotelProperty) {
        context.favoriteDataStore.edit { preferences ->
            val json = preferences[FAVORITE_KEY] ?: "[]"
            val type = object : TypeToken<List<HotelProperty>>() {}.type
            val currentList: MutableList<HotelProperty> = gson.fromJson(json, type) ?: mutableListOf()

            val existingIndex = currentList.indexOfFirst { it.name == hotel.name }

            if (existingIndex != -1) {
                currentList.removeAt(existingIndex)
            } else {
                currentList.add(0, hotel)
            }
            preferences[FAVORITE_KEY] = gson.toJson(currentList)
        }
    }
}