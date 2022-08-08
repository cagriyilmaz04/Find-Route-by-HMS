package com.example.hmscorekits.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("distance") @Expose val distance: Double,
    @SerializedName("distanceText") @Expose val distanceText: String,
    @SerializedName("duration") @Expose val duration: Double,
    @SerializedName("durationText") @Expose val durationText: String,
    @SerializedName("startLocation") @Expose val startLocation: Coordinate,
    @SerializedName("endLocation") @Expose val endLocation: Coordinate,
    @SerializedName("action") @Expose val action: String,
    @SerializedName("polyline") @Expose val polyline: ArrayList<Coordinate>,
    @SerializedName("roadName") @Expose val roadName: String,
    @SerializedName("orientation") @Expose val orientation: Int,
    @SerializedName("instruction") @Expose val instruction: String
)