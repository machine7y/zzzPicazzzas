package com.example.zzzpicazzzas.presentation.screen.detail

import com.example.zzzpicazzzas.presentation.base.mvvm.BaseState
import com.example.zzzpicazzzas.presentation.entity.PizzaUi

data class DetailUiState(
    val title: String = "",
    val selectedPizzaId: String = "",
    val pizzaList: List<PizzaUi> = emptyList(),
    val pizzaCount: Int = 0,
    val fullPrice: Float = 0f,
    val isZoomMode: Boolean = false,
) : BaseState {

    fun getSelectedPizzaIndex() = pizzaList.indexOfFirst { it.id == selectedPizzaId }

    fun getSelectedPizza() = pizzaList[getSelectedPizzaIndex()]
}
