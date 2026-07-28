package com.example.zzzpicazzzas.domain.uscase

import com.example.zzzpicazzzas.domain.base.usecase.BaseParamsUseCase
import com.example.zzzpicazzzas.domain.entity.PizzaList
import com.example.zzzpicazzzas.domain.repository.PizzaLocalSource
import javax.inject.Inject

class SaveLocalPizzaListUseCase @Inject constructor(
    private val pizzaLocalSource: PizzaLocalSource,
) : BaseParamsUseCase<PizzaList, Unit>() {

    override suspend fun execute(param: PizzaList) {
        pizzaLocalSource.savePizzaList(param)
    }
}
