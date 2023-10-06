package com.binar.foodorder.data.local.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.binar.foodorder.data.local.database.entity.CartEntity
import com.binar.foodorder.data.local.database.entity.FoodEntity
import com.binar.foodorder.model.Food

data class CartFoodRelation(
    @Embedded
    val cart: CartEntity,
    @Relation(parentColumn = "food_id", entityColumn = "id")
    val food: FoodEntity
)
