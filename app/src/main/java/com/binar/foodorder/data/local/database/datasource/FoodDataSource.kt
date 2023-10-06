package com.binar.foodorder.data.local.database.datasource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.binar.foodorder.data.local.database.dao.FoodDao
import com.binar.foodorder.data.local.database.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

interface FoodDataSource {
    fun getAllFoods(): Flow<List<FoodEntity>>
    fun getProductById(id: Int): Flow<FoodEntity>
    suspend fun insertProduct(food: List<FoodEntity>)
    suspend fun deleteProduct(food: FoodEntity): Int
    suspend fun updateProduct(food: FoodEntity): Int
}

class FoodDatabaseDataSource(private val dao: FoodDao) : FoodDataSource {
    override fun getAllFoods(): Flow<List<FoodEntity>> {
        return dao.getAllFoods()
    }

    override fun getProductById(id: Int): Flow<FoodEntity> {
        return dao.getProductById(id)
    }

    override suspend fun insertProduct(food: List<FoodEntity>) {
        return dao.insertProduct(food)
    }

    override suspend fun deleteProduct(food: FoodEntity): Int {
        return dao.deleteProduct(food)
    }

    override suspend fun updateProduct(food: FoodEntity): Int {
        return dao.updateProduct(food)
    }

}