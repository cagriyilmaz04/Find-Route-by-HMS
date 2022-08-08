package com.example.hmscorekits.data.model

import com.google.gson.annotations.SerializedName
import com.huawei.hms.maps.model.LatLng

data class Coordinate(
     @SerializedName("lng")
     var lng:Double,
     @SerializedName("lat")
     var lat:Double
 ) {
    fun toLatLng(): LatLng? {
        return LatLng(this.lat,this.lng)
    }
}


