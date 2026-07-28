package com.example.zzzpicazzzas.domain.uscase

import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.base.usecase.BaseNoParamsUseCase
import com.example.zzzpicazzzas.domain.entity.PizzaList
import com.example.zzzpicazzzas.domain.repository.PizzaLocalSource
import javax.inject.Inject

class GetLocalPizzaListUseCase @Inject constructor(
    private val pizzaLocalSource: PizzaLocalSource,
) : BaseNoParamsUseCase<ResultValue<PizzaList>>() {

    override suspend fun execute() = pizzaLocalSource.loadPizzaList()
}
