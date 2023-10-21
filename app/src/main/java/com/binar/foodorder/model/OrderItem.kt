package com.binar.foodorder.model

import com.google.gson.annotations.SerializedName

data class OrderItem(
    val catatan: String,
    val harga: Int,
    val nama: String,
    val qty: Int
)
