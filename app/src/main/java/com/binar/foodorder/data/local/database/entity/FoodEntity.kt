package com.binar.foodorder.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image")
    val Image: String,
    @ColumnInfo(name = "price")
    val Price: Double,
    @ColumnInfo(name = "description")
    val description: String,
)
