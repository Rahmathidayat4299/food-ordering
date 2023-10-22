package com.binar.foodorder.data.network.api

import android.util.Log
import com.binar.foodorder.data.network.model.FoodResponse
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.data.network.model.OrderResponse


interface FoodNetworkDataSource {
    suspend fun getFoods(): FoodResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class FoodNetworkDataSourceImpl(
    private val service: FoodService
) : FoodNetworkDataSource {
    override suspend fun getFoods(): FoodResponse {
        return service.getFoods()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }

}