package com.example.zzzpicazzzas.presentation.screen.detail

import com.example.zzzpicazzzas.presentation.base.mvvm.BaseEvent

sealed interface DetailEvent : BaseEvent {

    data class OnPizzaSelected(
        val pizzaUrl: String,
    ): DetailEvent

    data class OnSizeClicked(
        val size: String,
    ): DetailEvent

    data object OnPlusClicked: DetailEvent

    data object OnMinusClicked: DetailEvent
}
