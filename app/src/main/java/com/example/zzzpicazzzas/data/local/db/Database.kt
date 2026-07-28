package com.example.zzzpicazzzas.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zzzpicazzzas.data.local.db.dao.PizzaDao
import com.example.zzzpicazzzas.data.local.db.entity.PizzaEntity
import com.example.zzzpicazzzas.data.local.db.entity.VariantEntity

@Database(entities = [PizzaEntity::class, VariantEntity::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun pizzaDao(): PizzaDao
}
