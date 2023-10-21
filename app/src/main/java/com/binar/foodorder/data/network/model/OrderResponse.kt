package com.binar.foodorder.data.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderResponse(
    @SerializedName("code")
    val code:Int,
    @SerializedName("message")
    val message:String,
    @SerializedName("status")
    val status:Boolean,
)
