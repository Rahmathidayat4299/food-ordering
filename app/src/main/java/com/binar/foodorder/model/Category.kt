package com.binar.foodorder.model

import com.binar.foodorder.data.network.model.CategoryViewParam
import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Created by Rahmat Hidayat on 02/10/2023.
 */
data class Category(
    val imageUrl: String,
    val nama: String
)

fun CategoryViewParam.toCategory() = Category(
    imageUrl = this.imageUrl,
    nama = this.nama
)

fun Collection<CategoryViewParam>.toCategory() = this.map { it.toCategory() }