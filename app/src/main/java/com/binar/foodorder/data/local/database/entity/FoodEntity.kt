package com.binar.foodorder.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    val nama: String,
    @ColumnInfo(name = "image")
    val imageUrl: String,
    @ColumnInfo(name = "price")
    val hargaFormat: String,
    @ColumnInfo(name = "description")
    val detail: String,
    @ColumnInfo(name = "harga")
    val harga: Int,
    @ColumnInfo(name="alamat_resto")
    val alamatResto: String,
)
