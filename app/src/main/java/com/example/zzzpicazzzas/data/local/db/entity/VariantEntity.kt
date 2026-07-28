package com.example.zzzpicazzzas.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

const val VARIANT_TABLE = "variant"

const val COLUMN_PIZZA_ID = "pizza_id"
private const val COLUMN_PRICE = "price"
private const val COLUMN_SIZE = "size"

@Entity(
    tableName = VARIANT_TABLE,
    primaryKeys = [COLUMN_PIZZA_ID, COLUMN_PRICE, COLUMN_SIZE],
)
data class VariantEntity(
    @ColumnInfo(name = COLUMN_PIZZA_ID)
    val pizzaId: String,
    @ColumnInfo(name = COLUMN_PRICE)
    val price: Double,
    @ColumnInfo(name = COLUMN_SIZE)
    val size: String,
)
