package com.binar.foodorder.data.network.api

import com.binar.foodorder.data.network.model.FoodResponse
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.data.network.model.OrderResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FoodNetworkDataSourceImplTest {
    @MockK
    lateinit var service: FoodService

    private lateinit var dataSource: FoodNetworkDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FoodNetworkDataSourceImpl(service)
    }

    @Test
    fun getFoods() {
        runTest {
            val mockResponse = mockk<FoodResponse>()
            coEvery { service.getFoods() } returns mockResponse
            val response = dataSource.getFoods()
            coVerify { service.getFoods() }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            coVerify { service.createOrder(any()) }
            assertEquals(response, mockResponse)
        }
    }
}
