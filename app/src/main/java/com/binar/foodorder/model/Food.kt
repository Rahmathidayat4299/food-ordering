package com.binar.foodorder.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.binar.foodorder.data.local.database.entity.FoodEntity
import com.binar.foodorder.data.network.model.FoodViewParam
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rahmat Hidayat on 27/08/2023.
 */
@Keep
@Parcelize
data class Food(
    val id: Int,
    val imageUrl: String,
    val nama: String,
    val hargaFormat: String,
    val harga: Int,
    val detail: String,
    val alamatResto: String
) : Parcelable

fun FoodViewParam.toFood() = Food(
    id = this.id,
    imageUrl = this.imageUrl,
    nama = this.nama,
    hargaFormat = this.hargaFormat,
    harga = this.harga,
    detail = this.detail,
    alamatResto = this.alamatResto

)

fun Food.toFoodEntity() = FoodEntity(
    id = this.id,
    imageUrl = this.imageUrl,
    nama = this.nama,
    hargaFormat = this.hargaFormat,
    harga = this.harga,
    detail = this.detail,
    alamatResto = this.alamatResto
)

fun Collection<FoodViewParam>.toFood() = this.map { it.toFood() }
fun List<Food>.toFoodEntity() = this.map { it.toFoodEntity() }
