package com.example.zzzpicazzzas.data.remote.service

import com.example.zzzpicazzzas.domain.entity.PizzaList
import retrofit2.http.GET


interface PizzaServiceApi {

    @GET("pizzas")
    suspend fun loadPizzaList(): PizzaList
}
