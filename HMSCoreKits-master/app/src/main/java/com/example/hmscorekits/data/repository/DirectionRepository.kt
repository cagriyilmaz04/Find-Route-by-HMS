package com.example.hmscorekits.data.repository

import com.example.DirectionRequestModel
import com.example.hmscorekits.data.model.DirectionResponse

interface DirectionRepository {
    suspend fun getDrivingRoute(directionRequestModel: DirectionRequestModel): DirectionResponse

    suspend fun getCyclingRoute(directionRequestModel: DirectionRequestModel):DirectionResponse

    suspend fun getWalkingRoute(directionRequestModel: DirectionRequestModel):DirectionResponse

}