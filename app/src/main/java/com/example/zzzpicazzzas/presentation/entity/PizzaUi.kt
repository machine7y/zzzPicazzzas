package com.example.zzzpicazzzas.presentation.entity

data class PizzaUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val variants: List<VariantUi>,
) {

    fun getSelectedVariant() = variants.first { it.isSelected }
}
