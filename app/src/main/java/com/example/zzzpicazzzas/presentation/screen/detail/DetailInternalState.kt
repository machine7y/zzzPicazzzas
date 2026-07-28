package com.example.zzzpicazzzas.presentation.screen.detail

import com.example.zzzpicazzzas.domain.entity.PizzaList
import com.example.zzzpicazzzas.presentation.base.mvvm.BaseInternalState

data class DetailInternalState(
    val pizzaList: PizzaList = PizzaList(pizzas = emptyList()),
) : BaseInternalState
