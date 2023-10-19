package com.binar.foodorder.data.network.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryViewParamResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: List<CategoryViewParam>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)