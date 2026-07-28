package com.example.zzzpicazzzas.data.di

import com.example.zzzpicazzzas.data.local.source.PizzaLocalSourceImpl
import com.example.zzzpicazzzas.data.remote.source.PizzaRemoteSourceImpl
import com.example.zzzpicazzzas.domain.repository.PizzaLocalSource
import com.example.zzzpicazzzas.domain.repository.PizzaRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    abstract fun bindRemotePizzaSource(source: PizzaRemoteSourceImpl): PizzaRemoteSource

    @Binds
    abstract fun bindLocalPizzaSource(source: PizzaLocalSourceImpl): PizzaLocalSource
}
