package com.example.zzzpicazzzas.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pizza(
    @param:Json(name = "id")
    val id: String,
    @param:Json(name = "name")
    val name: String,
    @param:Json(name = "image_url")
    val imageUrl: String,
    @param:Json(name = "default_size")
    val defaultSize: String,
    @param:Json(name = "description")
    val description: String,
    @param:Json(name = "variants")
    val variants: List<Variant>
)
