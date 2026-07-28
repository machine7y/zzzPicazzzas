package com.example.zzzpicazzzas.data.di

import com.example.zzzpicazzzas.data.ENDPOINT
import com.example.zzzpicazzzas.data.remote.service.PizzaServiceApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi) = Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun providePizzaServiceApi(retrofit: Retrofit) = retrofit.create(PizzaServiceApi::class.java)
}
