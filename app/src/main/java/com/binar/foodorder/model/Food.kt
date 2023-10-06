package com.binar.foodorder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rahmat Hidayat on 27/08/2023.
 */
@Parcelize
data class Food(
    val id: Int? = null,
    val name:String,
    val Image:String,
    val Price:Double,
    val description:String,
):Parcelable
