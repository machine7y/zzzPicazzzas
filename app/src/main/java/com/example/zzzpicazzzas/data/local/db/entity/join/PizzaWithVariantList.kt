package com.example.zzzpicazzzas.data.local.db.entity.join

import androidx.room.Embedded
import androidx.room.Relation
import com.example.zzzpicazzzas.data.local.db.entity.COLUMN_ID
import com.example.zzzpicazzzas.data.local.db.entity.COLUMN_PIZZA_ID
import com.example.zzzpicazzzas.data.local.db.entity.PizzaEntity
import com.example.zzzpicazzzas.data.local.db.entity.VariantEntity

data class PizzaWithVariantList(
    @Embedded
    val pizza: PizzaEntity,
    @Relation(
        parentColumn = COLUMN_ID,
        entityColumn = COLUMN_PIZZA_ID,
    )
    val variantList: List<VariantEntity>
)
