package com.example.zzzpicazzzas.data.local.source

import com.example.zzzpicazzzas.data.local.db.dao.PizzaDao
import com.example.zzzpicazzzas.data.local.db.entity.PizzaEntity
import com.example.zzzpicazzzas.data.local.db.entity.VariantEntity
import com.example.zzzpicazzzas.data.local.db.entity.join.PizzaWithVariantList
import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.entity.Pizza
import com.example.zzzpicazzzas.domain.entity.PizzaList
import com.example.zzzpicazzzas.domain.entity.Variant
import com.example.zzzpicazzzas.domain.repository.PizzaLocalSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val EMPTY_PIZZA_DATA_MESSAGE = "Pizza data is empty"

class PizzaLocalSourceImpl @Inject constructor(
    private val pizzaDao: PizzaDao,
) : PizzaLocalSource {

    override suspend fun savePizzaList(pizzaList: PizzaList) {
        withContext(Dispatchers.IO) {
            val pizzaDataList = pizzaList.map()
            pizzaDao.upsertPizzaWithVariants(pizzaDataList)
        }
    }

    override suspend fun loadPizzaList(): ResultValue<PizzaList> = withContext(Dispatchers.IO) {
        val pizzaWithVariantList = pizzaDao.getPizzaWithVariantList()

        if (pizzaWithVariantList.isEmpty()) {
            ResultValue.Error(Exception(EMPTY_PIZZA_DATA_MESSAGE))
        } else {
            ResultValue.Success(pizzaWithVariantList.map())
        }
    }

    private fun PizzaList.map(): List<PizzaWithVariantList> = pizzas.map { pizza ->
        PizzaWithVariantList(
            pizza = PizzaEntity(
                id = pizza.id,
                name = pizza.name,
                imageUrl = pizza.imageUrl,
                defaultSize = pizza.defaultSize,
                description = pizza.description,
            ),
            variantList = pizza.variants.map { variant ->
                VariantEntity(
                    pizzaId = pizza.id,
                    price = variant.price,
                    size = variant.size,
                )
            }
        )
    }

    private fun List<PizzaWithVariantList>.map(): PizzaList = PizzaList(
        pizzas = map { pizzaWithVariants ->
            val pizzaEntity = pizzaWithVariants.pizza

            Pizza(
                id = pizzaEntity.id,
                name = pizzaEntity.name,
                imageUrl = pizzaEntity.imageUrl,
                defaultSize = pizzaEntity.defaultSize,
                description = pizzaEntity.description,
                variants = pizzaWithVariants.variantList.map { variantEntity ->
                    Variant(
                        price = variantEntity.price,
                        size = variantEntity.size,
                    )
                },
            )
        }
    )
}
