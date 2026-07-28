package com.example.zzzpicazzzas.presentation.di

import com.arttttt.nav3router.Router
import com.example.zzzpicazzzas.presentation.screen.Screen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideRouter() = Router<Screen>()
}