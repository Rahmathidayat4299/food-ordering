package com.binar.foodorder.data.network.api

import com.binar.foodorder.data.network.model.CategoryViewParamResponse
import com.binar.foodorder.data.network.model.FoodResponse

interface CategoryNetworkDataSource {
    suspend fun getCategoryListFoods():CategoryViewParamResponse
    suspend fun getCategory(category:String): FoodResponse
}

class CategoryNetworkDataSourceImpl(
    private val service: FoodService
):CategoryNetworkDataSource{
    override suspend fun getCategoryListFoods(): CategoryViewParamResponse {
        return service.getListCategories()
    }

    override suspend fun getCategory(category: String): FoodResponse {
        return service.getCategories(category)
    }


}