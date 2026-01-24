package com.example.travelapp.ui.theme.api

import retrofit2.http.GET
import retrofit2.http.Query

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
}

