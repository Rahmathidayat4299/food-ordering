package com.binar.foodorder.data.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderItem(
    @SerializedName("catatan")
    val catatan: String,
    @SerializedName("harga")
    val harga: Int,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("qty")
    val qty: Int
)
