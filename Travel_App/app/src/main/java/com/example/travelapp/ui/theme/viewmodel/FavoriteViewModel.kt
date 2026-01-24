package com.example.travelapp.ui.theme.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.ui.theme.api.HotelProperty
import com.example.travelapp.ui.theme.data.FavoriteDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private val _favorites = mutableStateListOf<HotelProperty>()
    val favorites: List<HotelProperty> = _favorites

    fun toggleFavorite(hotel: HotelProperty) {
        if (_favorites.any { it.name == hotel.name }) {
            _favorites.removeAll { it.name == hotel.name }
        } else {
            _favorites.add(hotel)
        }
    }

    fun isFavorite(hotel: HotelProperty): Boolean {
        return _favorites.any { it.name == hotel.name }
    }
}
