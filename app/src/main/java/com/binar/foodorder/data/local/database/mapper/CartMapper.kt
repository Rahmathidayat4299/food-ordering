package com.binar.foodorder.data.local.database.mapper

import com.binar.foodorder.data.local.database.entity.CartEntity
import com.binar.foodorder.model.Cart

fun CartEntity?.toCart() = Cart(
    id = this?.id ?: 0,
    foodId = this?.foodId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    itemNotes = this?.itemNotes.orEmpty(),
    foodImgUrl = this?.foodImgUrl.orEmpty(),
    foodName = this?.foodName.orEmpty(),
    foodPrice = this?.foodPrice ?: 0.0
)

// View Object > Entity
fun Cart?.toCartEntity() = CartEntity(
    id = this?.id,
    foodId = this?.foodId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    itemNotes = this?.itemNotes.orEmpty(),
    foodImgUrl = this?.foodImgUrl.orEmpty(),
    foodName = this?.foodName.orEmpty(),
    foodPrice = this?.foodPrice ?: 0.0
)

// list of entity > list of view object
fun List<CartEntity?>.toCartList() = this.map { it.toCart() }
