package com.binar.foodorder.data.repository

import com.binar.foodorder.data.local.database.AppDatabase
import com.binar.foodorder.data.network.api.CategoryNetworkDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.data.network.model.OrderResponse
import com.binar.foodorder.model.Category
import com.binar.foodorder.model.Food
import com.binar.foodorder.model.toCategory
import com.binar.foodorder.model.toFood
import com.binar.foodorder.util.ResultWrapper
import com.binar.foodorder.util.proceedFlow
import kotlinx.coroutines.flow.Flow

interface FoodsRepository {
    suspend fun getFoods(): Flow<ResultWrapper<List<Food>>>
    suspend fun getListCategory(): Flow<ResultWrapper<List<Category>>>
    suspend fun getCategory(category:String): Flow<ResultWrapper<List<Food>>>


}

class FoodsRepositoryImpl(
    private val foodNetworkDataSource: FoodNetworkDataSource,
    private val categoryNetworkDataSource: CategoryNetworkDataSource,
    private val appDatabase: AppDatabase
) : FoodsRepository {
    override suspend fun getFoods(): Flow<ResultWrapper<List<Food>>> {
        return proceedFlow {
            foodNetworkDataSource.getFoods().data.toFood()
        }
    }

    override suspend fun getListCategory(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            categoryNetworkDataSource.getCategoryListFoods().data.toCategory()
        }
    }

    override suspend fun getCategory(category: String): Flow<ResultWrapper<List<Food>>> {
        return proceedFlow {
            categoryNetworkDataSource.getCategory(category).data.toFood()
        }
    }




}