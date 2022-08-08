package com.example.hmscorekits.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.DirectionRequestModel
import com.example.hmscorekits.data.model.Coordinate
import com.example.hmscorekits.data.model.DirectionResponse
import com.example.hmscorekits.data.model.Route
import com.example.hmscorekits.data.repository.DirectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val directionRepository: DirectionRepository

) : ViewModel() {
    var originCoordinate: Coordinate? = null
    var destinationCoordinate: Coordinate? = null
    var aaa = 5
    var tripRoute = MutableLiveData<Route>()

    val originCoordinateLive: MutableLiveData<Coordinate> = MutableLiveData<Coordinate>()
    val destinationCoordinateLive: MutableLiveData<Coordinate> = MutableLiveData<Coordinate>()

    fun setAllOriginCoordinate(coordinate: Coordinate) {
        originCoordinate = coordinate
        originCoordinateLive.value = coordinate
    }

    fun setAllDestinationCoordinate(coordinate: Coordinate) {
        destinationCoordinate = coordinate
        destinationCoordinateLive.value = coordinate
    }


    suspend fun getDrivingRoute(directionRequestModel: DirectionRequestModel): DirectionResponse {
        return directionRepository.getDrivingRoute(directionRequestModel)
    }

    suspend fun getCyclingRoute(directionRequestModel: DirectionRequestModel): DirectionResponse {
        return directionRepository.getCyclingRoute(directionRequestModel)
    }

    suspend fun getWalkingRoute(directionRequestModel: DirectionRequestModel): DirectionResponse {
        return directionRepository.getWalkingRoute(directionRequestModel)
    }
}



