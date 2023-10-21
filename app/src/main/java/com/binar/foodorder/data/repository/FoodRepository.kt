package com.binar.foodorder.data.repository

import com.binar.foodorder.data.dummy.DummyCategoryDataSource
import com.binar.foodorder.data.local.database.datasource.FoodDataSource
import com.binar.foodorder.data.local.database.mapper.toFoodList
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.data.network.model.OrderResponse
import com.binar.foodorder.model.Category
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.ResultWrapper
import com.binar.foodorder.util.proceed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface FoodRepository {
    fun getFoods(): Flow<ResultWrapper<List<Food>>>
    fun getCategories(): List<Category>

}

class FoodRepositoryImpl(
    private val foodDataSource: FoodDataSource,
    private val dummyCategoryDataSource: DummyCategoryDataSource,

) : FoodRepository {
    override fun getFoods(): Flow<ResultWrapper<List<Food>>> {
        return foodDataSource.getAllFoods().map {
            proceed { it.toFoodList() }
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

    override fun getCategories(): List<Category> {
        return dummyCategoryDataSource.getFoodCategory()
    }


}