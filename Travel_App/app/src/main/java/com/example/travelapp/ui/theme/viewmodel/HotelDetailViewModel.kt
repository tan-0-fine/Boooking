package com.example.travelapp.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelapp.ui.theme.api.HotelProperty

class HotelDetailViewModel : ViewModel() {

    private val _hotel = mutableStateOf<HotelProperty?>(null)
    val hotel: HotelProperty?
        get() = _hotel.value

    fun setHotel(hotel: HotelProperty) {
        _hotel.value = hotel
    }
}
