package com.example.hmscorekits

import android.app.Application
import com.huawei.hms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.setApiKey("DAEDAKCxWz+NeVtZko8x0E0cg3zZpyVXm04gyCrTt3BSrWNrr4EjfYVuv05vaFkU4BIp+/gVq9u12doOpunXyBSQKwFPq7dhhWFD+w==")

    }

}
