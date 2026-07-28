package com.example.zzzpicazzzas.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Variant(
    @param:Json(name = "price")
    val price: Double,
    @param:Json(name = "size")
    val size: String,
)
