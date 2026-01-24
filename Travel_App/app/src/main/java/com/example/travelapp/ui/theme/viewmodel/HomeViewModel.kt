package com.example.travelapp.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    var selectedLocation by mutableStateOf("Hồ Chí Minh, Việt Nam")
        private set

    fun updateLocation(location: String) {
        selectedLocation = location
    }
}

