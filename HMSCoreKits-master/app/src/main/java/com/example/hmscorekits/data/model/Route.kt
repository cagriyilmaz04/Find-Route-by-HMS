package com.example.hmscorekits.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.huawei.hms.site.api.model.CoordinateBounds

data class Route(
    @SerializedName("paths") @Expose val paths: ArrayList<Path>,
    @SerializedName("optimizedWaypoints") @Expose val optimizedWaypoints: ArrayList<Int>,
    @SerializedName("bounds") @Expose val bounds: CoordinateBounds,
    @SerializedName("hasRestrictedRoad") @Expose val hasRestrictedRoad: Int,
    @SerializedName("dstInRestrictedArea") @Expose val dstInRestrictedArea: Int,
    @SerializedName("crossCountry") @Expose val crossCountry: Int,
    @SerializedName("crossMultiCountries") @Expose val crossMultiCountries: Int,
    @SerializedName("hasRoughRoad") @Expose val hasRoughRoad: Int,
    @SerializedName("dstInDiffTimeZone") @Expose val dstInDiffTimeZone: Int,
    @SerializedName("hasFerry") @Expose val hasFerry: Int,
    @SerializedName("hasTrafficLight") @Expose val hasTrafficLight: Int,
    @SerializedName("hasTolls") @Expose val hasTolls: Int,
    @SerializedName("trafficLightNum") @Expose val trafficLightNum: Int
)