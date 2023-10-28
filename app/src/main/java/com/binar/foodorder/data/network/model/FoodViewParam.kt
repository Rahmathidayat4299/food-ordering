package com.binar.foodorder.data.network.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rahmat Hidayat on 27/08/2023.
 */
@Keep
@Parcelize
data class FoodViewParam(
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("harga_format")
    val hargaFormat: String,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("alamat_resto")
    val alamatResto: String,
    @SerializedName("harga")
    val harga: Int
) : Parcelable

@Keep

data class FoodResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<FoodViewParam>
)
