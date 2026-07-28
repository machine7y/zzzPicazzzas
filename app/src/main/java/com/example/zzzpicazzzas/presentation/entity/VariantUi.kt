package com.example.zzzpicazzzas.presentation.entity

data class VariantUi(
    val size: String,
    val isSelected: Boolean,
) {

    companion object {

        const val SIZE_SMALL = "S"
        const val SIZE_MEDIUM = "M"
        const val SIZE_LARGE = "L"
    }
}
