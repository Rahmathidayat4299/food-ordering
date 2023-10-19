package com.binar.foodorder.data.network.api

import android.util.Log
import com.binar.foodorder.data.network.model.FoodResponse


interface FoodNetworkDataSource {
    suspend fun getFoods(): FoodResponse
}

class FoodNetworkDataSourceImpl(
    private val service: FoodService
) : FoodNetworkDataSource {
    override suspend fun getFoods(): FoodResponse {
        return service.getFoods()
    }

}