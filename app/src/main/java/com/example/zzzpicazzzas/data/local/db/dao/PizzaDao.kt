package com.example.zzzpicazzzas.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.zzzpicazzzas.data.local.db.entity.PIZZA_TABLE
import com.example.zzzpicazzzas.data.local.db.entity.PizzaEntity
import com.example.zzzpicazzzas.data.local.db.entity.join.PizzaWithVariantList
import com.example.zzzpicazzzas.data.local.db.entity.VariantEntity

@Dao
abstract class PizzaDao {

    @Transaction
    @Query("SELECT * FROM $PIZZA_TABLE")
    abstract suspend fun getPizzaWithVariantList(): List<PizzaWithVariantList>

    @Upsert
    abstract suspend fun upsertPizzas(pizzas: List<PizzaEntity>)

    @Upsert
    abstract suspend fun upsertVariants(variants: List<VariantEntity>)

    @Transaction
    open suspend fun upsertPizzaWithVariants(items: List<PizzaWithVariantList>) {
        upsertPizzas(items.map { it.pizza })
        upsertVariants(items.flatMap { it.variantList })
    }
}
