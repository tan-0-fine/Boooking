package com.example.travelapp.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.data.FavoriteStorage
import com.example.travelapp.ui.theme.api.HotelProperty
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = FavoriteStorage(application)

    // Đổi Hotel -> HotelProperty
    val favoriteHotels: StateFlow<List<HotelProperty>> = storage.getFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Đổi Hotel -> HotelProperty
    fun toggleFavorite(hotel: HotelProperty) {
        viewModelScope.launch {
            storage.toggleFavorite(hotel)
        }
    }
}