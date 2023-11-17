package com.binar.foodorder.data.network.api

import com.binar.foodorder.data.network.model.CategoryViewParamResponse
import com.binar.foodorder.data.network.model.FoodResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CategoryNetworkDataSourceImplTest {
    @MockK
    lateinit var service: FoodService
    private lateinit var dataSource: CategoryNetworkDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = CategoryNetworkDataSourceImpl(service)
    }

    @Test
    fun getCategoryListFoods() {
        runTest {
            val mockResponse = mockk<CategoryViewParamResponse>(relaxed = true)
            coEvery { service.getListCategories() } returns mockResponse
            val response = dataSource.getCategoryListFoods()
            coVerify { service.getListCategories() }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategory() {
        runTest {
            val mockResponse = mockk<FoodResponse>()
            coEvery { service.getCategories(any()) } returns mockResponse
            val response = dataSource.getCategory("Category")
            coVerify { service.getCategories(any()) }
            assertEquals(response, mockResponse)
        }
    }
}
