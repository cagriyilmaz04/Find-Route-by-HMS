package com.example.hmscorekits.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class DirectionResponse(
    @SerializedName("returnCode") @Expose val returnCode: String,
    @SerializedName("returnDesc") @Expose val returnDesc: String,
    @SerializedName("routes") @Expose val routes: ArrayList<Route>
)