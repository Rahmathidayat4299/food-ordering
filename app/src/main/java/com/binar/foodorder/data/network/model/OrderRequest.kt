package com.binar.foodorder.data.network.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderRequest(
    @SerializedName("orders")
    val orders: List<OrderItem>,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("username")
    val username: String?
)