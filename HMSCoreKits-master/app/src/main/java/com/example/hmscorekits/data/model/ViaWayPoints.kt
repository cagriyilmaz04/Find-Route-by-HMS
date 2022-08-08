package com.example.hmscorekits.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ViaWayPoints(
    @SerializedName("location") @Expose val location: Coordinate,
    @SerializedName("stepIndex") @Expose val stepIndex: Int
)