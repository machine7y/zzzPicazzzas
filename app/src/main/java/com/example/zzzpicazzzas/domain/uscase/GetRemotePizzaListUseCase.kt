package com.example.zzzpicazzzas.domain.uscase

import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.base.usecase.BaseNoParamsUseCase
import com.example.zzzpicazzzas.domain.entity.PizzaList
import com.example.zzzpicazzzas.domain.repository.PizzaRemoteSource
import javax.inject.Inject

class GetRemotePizzaListUseCase @Inject constructor(
    private val pizzaRemoteSource: PizzaRemoteSource,
) : BaseNoParamsUseCase<ResultValue<PizzaList>>() {

    override suspend fun execute() = pizzaRemoteSource.getPizzaList()
}
