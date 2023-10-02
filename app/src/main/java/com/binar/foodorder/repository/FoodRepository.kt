package com.binar.foodorder.repository

import com.binar.foodorder.model.Category
import com.binar.foodorder.model.CategoryDataSource
import com.binar.foodorder.model.Food
import com.binar.foodorder.model.FoodDataSource

/**
 * Created by Rahmat Hidayat on 27/08/2023.
 */
interface FoodRepository {
    suspend fun getFood(): List<Food>
    fun getCategory(): List<Category>
}

class FoodRepositoryImpl(
    private val foodLocalDataSource: FoodDataSource,
    private val categoryDataSource: CategoryDataSource

) : FoodRepository {
    override suspend fun getFood(): List<Food> {
        return foodLocalDataSource.getFoodList()
    }

    override fun getCategory(): List<Category> {
        return categoryDataSource.getFoodCategory()
    }

}