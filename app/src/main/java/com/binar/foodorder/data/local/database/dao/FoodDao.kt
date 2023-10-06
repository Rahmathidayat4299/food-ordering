package com.binar.foodorder.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.binar.foodorder.data.local.database.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM FOODS")
    fun getAllFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM FOODS WHERE id == :id")
    fun getProductById(id: Int): Flow<FoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: List<FoodEntity>)

    @Delete
    suspend fun deleteProduct(food: FoodEntity): Int

    @Update
    suspend fun updateProduct(food: FoodEntity): Int
}