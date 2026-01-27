package com.example.travelapp.ui.theme.api

import com.google.gson.annotations.SerializedName

data class HotelResponse(
    val ads: List<HotelProperty>?,
    val properties: List<HotelProperty>?
)


data class SearchMetadata(
    val status: String?
)

data class HotelProperty(
    val name: String?,
    val thumbnail: String?,
    val overall_rating: Double?,
    val reviews: Int?,
    val price: String?,
    val extracted_price: Int?,
    val free_cancellation: Boolean?,
    val property_token: String?,
    @SerializedName("description")
    val description: String?,
    val location: String? = "Việt Nam",
    val imageRes: Int? = 0
)
