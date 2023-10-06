package com.binar.foodorder.data.local.database.mapper

import com.binar.foodorder.data.local.database.datasource.FoodDataSource
import com.binar.foodorder.data.local.database.entity.FoodEntity
import com.binar.foodorder.model.Food

fun FoodEntity?.toFood() = Food(
    id = this?.id ?: 0,
    name = this?.name.orEmpty(),
    Price = this?.Price ?: 0.0,
    Image = this?.Image.orEmpty(),
    description = this?.description.orEmpty()
)

fun Food?.FoodEntity() = FoodEntity(
    id = this?.id ?: 0,
    name = this?.name.orEmpty(),
    Price = this?.Price ?: 0.0,
    Image = this?.Image.orEmpty(),
    description = this?.description.orEmpty()
)

fun List<FoodEntity?>.toFoodList() = this.map { it.toFood() }
fun List<Food?>.toFoodEntity() = this.map { it.FoodEntity() }