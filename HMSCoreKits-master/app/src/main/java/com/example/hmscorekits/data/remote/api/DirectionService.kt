package com.example.hmscorekits.data.remote.api

import com.example.DirectionRequestModel
import com.example.hmscorekits.data.model.DirectionResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DirectionService {
    @POST("driving?key=DAEDAKCxWz%2BNeVtZko8x0E0cg3zZpyVXm04gyCrTt3BSrWNrr4EjfYVuv05vaFkU4BIp%2B%2FgVq9u12doOpunXyBSQKwFPq7dhhWFD%2Bw%3D%3D")
    suspend fun getDrivingRoute(@Body directionRequestModel: DirectionRequestModel): DirectionResponse

    @POST("bicycling?key=DAEDAKCxWz%2BNeVtZko8x0E0cg3zZpyVXm04gyCrTt3BSrWNrr4EjfYVuv05vaFkU4BIp%2B%2FgVq9u12doOpunXyBSQKwFPq7dhhWFD%2Bw%3D%3D")
    suspend fun getCyclingyclingRoute(@Body directionRequestModel: DirectionRequestModel): DirectionResponse

    @POST("walking?key=DAEDAKCxWz%2BNeVtZko8x0E0cg3zZpyVXm04gyCrTt3BSrWNrr4EjfYVuv05vaFkU4BIp%2B%2FgVq9u12doOpunXyBSQKwFPq7dhhWFD%2Bw%3D%3D")
    suspend fun getWalkingRoute(@Body directionRequestModel: DirectionRequestModel): DirectionResponse




}