package com.example.travelapp.ui.theme.data

import androidx.compose.runtime.mutableStateListOf
import com.example.travelapp.ui.theme.api.FlightItem
import com.example.travelapp.ui.theme.api.HotelProperty

object AppData {
    var currentHotel: HotelProperty? = null
    var currentSelectedRoom: RoomOption? = null
    var checkInDate: String = ""
    var checkOutDate: String = ""

    // Thêm dòng này
    var flightPrice: Double = 0.0
    var isFlightSelected: Boolean = false

    var hotelList: List<HotelProperty> = emptyList()
    var flightList: List<FlightItem> = emptyList()
    var currentFlight: FlightItem? = null
    var passengerInfo: Passengerinfo? = null // Lưu thông tin khách
    var selectedSeat: String = ""
    var passengerCount: Int = 1
    var searchDestination: String = ""
    var paymentType: String = ""
    val tripList = mutableStateListOf<Trip>()
}
