package com.example.hmscorekits.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Path(
    @SerializedName("steps") @Expose val steps: ArrayList<Step>,
    @SerializedName("distance") @Expose val distance: Double,
    @SerializedName("distanceText") @Expose val distanceText: String,
    @SerializedName("duration") @Expose val duration: Double,
    @SerializedName("durationText") @Expose val durationText: String,
    @SerializedName("durationInTraffic") @Expose val durationInTraffic: Double,
    @SerializedName("durationInTrafficText") @Expose val durationInTrafficText: String,
    @SerializedName("startLocation") @Expose val startLocation: Coordinate,
    @SerializedName("startAddress") @Expose val startAddress: String,
    @SerializedName("endLocation") @Expose val endLocation: Coordinate,
    @SerializedName("endAddress") @Expose val endAddress: String,
    @SerializedName("viaWaypoints") @Expose val viaWaypoints: ArrayList<ViaWayPoints>
)