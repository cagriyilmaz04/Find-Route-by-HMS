package com.example.hmscorekits.data.repository

import com.example.DirectionRequestModel
import com.example.hmscorekits.data.model.DirectionResponse
import com.example.hmscorekits.data.remote.api.DirectionService
import javax.inject.Inject



class DirectionRepositoryImp @Inject constructor(
    private val directionService: DirectionService
): DirectionRepository {
    override suspend fun getDrivingRoute(directionRequestModel: DirectionRequestModel): DirectionResponse {
        return directionService.getDrivingRoute(directionRequestModel)
    }

    override suspend fun getCyclingRoute(directionRequestModel: DirectionRequestModel): DirectionResponse {
        return directionService.getCyclingyclingRoute(directionRequestModel)
    }

    override suspend fun getWalkingRoute(directionRequestModel: DirectionRequestModel): DirectionResponse {
        return directionService.getWalkingRoute(directionRequestModel)
    }
}