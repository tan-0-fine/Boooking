package com.example.travelapp.ui.theme.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.travelapp.ui.theme.api.FlightResponse
interface HotelApiService {

    @GET("search.json")
    suspend fun searchHotels(
        @Query("engine") engine: String = "google_hotels",
        @Query("q") query: String,
        @Query("check_in_date") checkInDate: String,
        @Query("check_out_date") checkOutDate: String,
        @Query("adults") adults: Int = 2,
        @Query("currency") currency: String = "USD",
        @Query("hl") hl: String = "en",
        @Query("gl") gl: String = "us",
        @Query("api_key") apiKey: String
    ): HotelResponse

    @GET("search")
    suspend fun searchFlights(
        @Query("engine") engine: String = "google_flights",
        @Query("departure_id") departureId: String,
        @Query("arrival_id") arrivalId: String,
        @Query("outbound_date") date: String,

        // --- THÊM DÒNG NÀY ---
        @Query("type") type: String = "2", // số 2 quy định là Vé 1 chiều (One-way)
        @Query("gl") gl: String = "vn",    // Định vị người dùng ở Việt Nam
        // ---------------------

        @Query("currency") currency: String = "VND",
        @Query("hl") lang: String = "vi",
        @Query("api_key") apiKey: String,
        @Query("adults") adults: Int = 1,      // Số người lớn
        @Query("children") children: Int = 0,  // Số trẻ em
        @Query("travel_class") travelClass: Int = 1
    ): FlightResponse
}

