package com.example.zzzpicazzzas.presentation.utils

fun distributePoints(count: Int, spread: Float = 1.052f) = if (count <= 0) {
    emptyList()
} else {
    (1..count).map { index ->
        val base = index.toFloat() / (count + 1)
        0.5f + (base - 0.5f) * spread
    }
}
