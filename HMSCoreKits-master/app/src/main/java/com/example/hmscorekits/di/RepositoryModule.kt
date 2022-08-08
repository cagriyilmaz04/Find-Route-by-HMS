package com.example.hmscorekits.di

import com.example.hmscorekits.data.remote.api.DirectionService
import com.example.hmscorekits.data.repository.DirectionRepository
import com.example.hmscorekits.data.repository.DirectionRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideDirectionRepository(
        directionService: DirectionService
    ): DirectionRepository{
        return DirectionRepositoryImp(directionService)
    }
}