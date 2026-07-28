package com.example.zzzpicazzzas.presentation.screen.detail

import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.entity.Pizza
import com.example.zzzpicazzzas.domain.uscase.GetLocalPizzaListUseCase
import com.example.zzzpicazzzas.presentation.base.mvvm.BaseViewModel
import com.example.zzzpicazzzas.presentation.entity.PizzaUi
import com.example.zzzpicazzzas.presentation.entity.VariantUi
import com.example.zzzpicazzzas.presentation.screen.detail.DetailLabel.InternalLoadingException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getLocalPizzaListUseCase: GetLocalPizzaListUseCase,
) : BaseViewModel<DetailUiState, DetailInternalState, DetailEvent, DetailLabel>(
    DetailUiState(),
    DetailInternalState(),
) {

    init {
        loadLocalPizzaData()
    }

    override fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnPizzaSelected -> onPizzaSelected(event.pizzaUrl)
            is DetailEvent.OnSizeClicked -> onSizeClicked(event)
            DetailEvent.OnMinusClicked -> updateState { addToCount(1) }
            DetailEvent.OnPlusClicked -> updateState { addToCount(-1) }
        }
    }

    private fun onPizzaSelected(newSelectedPizzaUrl: String) {
        state.pizzaList.find { it.imageUrl == newSelectedPizzaUrl }?.id?.let { newSelectedPizzaId ->
            val variant = state.pizzaList.find { it.id == newSelectedPizzaId }
                ?.variants
                ?.find { it.isSelected }
            val price = internalState.pizzaList.pizzas.find { it.id == newSelectedPizzaId }
                ?.variants
                ?.find { it.size == variant?.size }
                ?.price
            val title = internalState.pizzaList.pizzas.find { it.id == newSelectedPizzaId }?.name ?: ""

            updateState {
                copy(
                    title = title,
                    isZoomMode = if (getSelectedPizza().id == newSelectedPizzaId) !isZoomMode else false,
                    selectedPizzaId = newSelectedPizzaId,
                    fullPrice = price?.let { (it * pizzaCount).toFloat() } ?: 0f
                )
            }
        }
    }

    private fun onSizeClicked(event: DetailEvent.OnSizeClicked) {
        updateState {
            val newPizzaList = pizzaList.map { pizza ->
                if (pizza.id == selectedPizzaId) {
                    pizza.copy(
                        variants = pizza.variants.map { variantUi ->
                            variantUi.copy(isSelected = variantUi.size == event.size)
                        }
                    )
                } else {
                    pizza
                }
            }
            val price = internalState.pizzaList.pizzas.find { it.id == selectedPizzaId }
                ?.variants
                ?.find { it.size == event.size }
                ?.price

            copy(
                pizzaList = newPizzaList,
                fullPrice = price?.let { (it * pizzaCount).toFloat() } ?: 0f
            )
        }
    }

    private fun loadLocalPizzaData() = launch {
        val pizzaListResult = getLocalPizzaListUseCase()
        if (pizzaListResult is ResultValue.Success) {
            updateInternalState { copy(pizzaList = pizzaListResult.data) }
            updateState {
                val pizzas = pizzaListResult.data.pizzas
                val selectedPizzaIndex = if (pizzas.size >= 3) 1 else 0
                val selectedPizza = pizzas[selectedPizzaIndex]
                val selectedPizzaVariantList = selectedPizza.variants
                val selectedVariant = selectedPizzaVariantList.find { it.size == selectedPizza.defaultSize }

                copy(
                    title = selectedPizza.name,
                    selectedPizzaId = selectedPizza.id,
                    pizzaList = pizzaListResult.data.pizzas.mapToUi(),
                    pizzaCount = 1,
                    fullPrice = selectedVariant?.price?.toFloat() ?: 0f,
                )
            }
        } else {
            publishLabel(InternalLoadingException)
        }
    }

    private fun DetailUiState.addToCount(delta: Int): DetailUiState {
        val selectedPizza = getSelectedPizza()
        val selectedPizzaId = selectedPizza.id
        val selectedVariant = selectedPizza.getSelectedVariant()
        val nextValue = pizzaCount + delta

        val newPizzaCount = when {
            nextValue >= 99 -> 99
            nextValue >= 1 -> nextValue
            else -> 1 // nextValue < 1
        }

        val variantPrice = internalState.pizzaList.pizzas
            .find { it.id == selectedPizzaId }
            ?.variants
            ?.find { it.size == selectedVariant.size }
            ?.price

        return variantPrice?.let {
            copy(
                pizzaCount = newPizzaCount,
                fullPrice = (variantPrice * newPizzaCount).toFloat(),
            )
        } ?: copy()
    }

    private fun List<Pizza>.mapToUi() = map { pizza ->
        PizzaUi(
            id = pizza.id,
            name = pizza.name,
            imageUrl = pizza.imageUrl,
            description = pizza.description,
            variants = pizza.variants.map { variant ->
                VariantUi(
                    size = variant.size,
                    isSelected = variant.size == pizza.defaultSize,
                )
            },
        )
    }
}
