package com.example.zzzpicazzzas.domain.repository

import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.entity.PizzaList

interface PizzaRemoteSource {

    suspend fun getPizzaList(): ResultValue<PizzaList>
}
