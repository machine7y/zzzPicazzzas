package com.example.zzzpicazzzas.data.remote.source

import com.example.zzzpicazzzas.data.remote.service.PizzaServiceApi
import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.repository.PizzaRemoteSource
import javax.inject.Inject

class PizzaRemoteSourceImpl @Inject constructor(
    private val pizzaServiceApi: PizzaServiceApi,
) : PizzaRemoteSource {

    override suspend fun getPizzaList() = try {
        val response = pizzaServiceApi.loadPizzaList()

        ResultValue.Success(response)
    } catch (e: Exception) {
        ResultValue.Error(e)
    }
}
