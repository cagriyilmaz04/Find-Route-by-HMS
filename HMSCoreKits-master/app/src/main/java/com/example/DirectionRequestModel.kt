package com.example

import com.example.hmscorekits.data.model.Coordinate
import com.google.gson.annotations.SerializedName

data class DirectionRequestModel (
    @SerializedName("origin")
    var origin: Coordinate,
    @SerializedName("destination")
    var destination: Coordinate
    )