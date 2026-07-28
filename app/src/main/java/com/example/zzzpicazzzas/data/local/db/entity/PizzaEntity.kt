package com.example.zzzpicazzzas.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

const val PIZZA_TABLE = "pizza"

const val COLUMN_ID = "id"

@Entity(
    tableName = PIZZA_TABLE,
    primaryKeys = [COLUMN_ID],
)
data class PizzaEntity(
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "default_size")
    val defaultSize: String,
    @ColumnInfo(name = "description")
    val description: String,
)
