package com.example.travelapp.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelapp.ui.theme.api.HotelProperty

class HotelDetailViewModel : ViewModel() {

    var hotel by mutableStateOf<HotelProperty?>(null)
        private set

    fun selectHotel(hotel: HotelProperty) {
        this.hotel = hotel
    }
}


