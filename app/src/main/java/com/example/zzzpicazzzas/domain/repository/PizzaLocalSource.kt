package com.example.zzzpicazzzas.domain.repository

import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.entity.PizzaList

interface PizzaLocalSource {

    suspend fun savePizzaList(pizzaList: PizzaList)

    suspend fun loadPizzaList(): ResultValue<PizzaList>
}
