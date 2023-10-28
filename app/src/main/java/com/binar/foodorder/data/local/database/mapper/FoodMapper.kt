package com.binar.foodorder.data.local.database.mapper

import com.binar.foodorder.data.local.database.entity.FoodEntity
import com.binar.foodorder.model.Food
import com.binar.foodorder.model.toFoodEntity

fun FoodEntity?.toFood() = Food(
    id = this?.id ?: 0,
    nama = this?.nama.orEmpty(),
    hargaFormat = this?.hargaFormat.toString(),
    imageUrl = this?.imageUrl.orEmpty(),
    detail = this?.detail.orEmpty(),
    alamatResto = this?.alamatResto.toString(),
    harga = this?.harga?.toInt() ?: 0
)

fun Food?.FoodEntity() = FoodEntity(
    id = this?.id ?: 0,
    nama = this?.nama.orEmpty(),
    hargaFormat = this?.hargaFormat.toString(),
    imageUrl = this?.imageUrl.orEmpty(),
    detail = this?.detail.orEmpty(),
    alamatResto = this?.alamatResto.toString(),
    harga = this?.harga?.toInt() ?: 0
)

fun List<FoodEntity?>.toFoodList() = this.map { it.toFood() }
fun List<Food?>.toFoodEntity() = this.map { it?.toFoodEntity() }
