package com.example.travelapp.ui.theme.api

import retrofit2.http.GET
import retrofit2.http.Query

interface HotelApiService {
    @GET("search.json") // Phần đuôi của link
    suspend fun getHotels(
        @Query("engine") engine: String = "google_hotels",
        @Query("q") query: String,
        @Query("check_in_date") checkIn: String,
        @Query("check_out_date") checkOut: String,
        @Query("currency") currency: String = "VND",
        @Query("api_key") apiKey: String // e5ca58b702415625737f21d18ee1b3c715d4023b43f23d606ec1949d2f715ee8
    ): HotelResponse
}