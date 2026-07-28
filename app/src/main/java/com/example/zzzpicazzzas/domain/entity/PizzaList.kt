package com.example.zzzpicazzzas.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PizzaList(
    @param:Json(name = "pizzas")
    val pizzas: List<Pizza>,
)
