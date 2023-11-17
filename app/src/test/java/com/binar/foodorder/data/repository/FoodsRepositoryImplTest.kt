package com.binar.foodorder.data.repository

import app.cash.turbine.test
import com.binar.foodorder.data.network.api.CategoryNetworkDataSource
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.model.CategoryViewParam
import com.binar.foodorder.data.network.model.CategoryViewParamResponse
import com.binar.foodorder.data.network.model.FoodResponse
import com.binar.foodorder.data.network.model.FoodViewParam
import com.binar.foodorder.util.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class FoodsRepositoryImplTest {
    @MockK
    lateinit var remoteDataSourceFood: FoodNetworkDataSource

    @MockK
    lateinit var remoteDataSourceCategory: CategoryNetworkDataSource

    private lateinit var repository: FoodsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = FoodsRepositoryImpl(remoteDataSourceFood, remoteDataSourceCategory)
    }

    @Test
    fun `get categories, with result loading`() {
        val mockCategoryResponse = mockk<CategoryViewParamResponse>()
        runTest {
            coEvery { remoteDataSourceCategory.getCategoryListFoods() } returns mockCategoryResponse
            repository.getListCategory().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSourceCategory.getCategoryListFoods() }
            }
        }
    }

    @Test
    fun `get categories, with result success`() {
        val fakeCategoryResponse = CategoryViewParam(
            imageUrl = "url",
            nama = "name"
        )
        val fakeFood = FoodViewParam(
            id = 1,
            imageUrl = "url",
            nama = "url",
            hargaFormat = "url",
            harga = 1,
            detail = "url",
            alamatResto = "url"
        )
        val fakeCategoriesResponse = FoodResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeFood)
        )
        runTest {
            coEvery { remoteDataSourceCategory.getCategory(any()) } returns fakeCategoriesResponse
            repository.getCategory("category").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                coVerify { remoteDataSourceCategory.getCategory(any()) }
            }
        }
    }

    @Test
    fun `get categories, with result empty`() {
        val fakeCategoriesResponse = FoodResponse(
            code = 200,
            status = true,
            message = "Success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSourceCategory.getCategory(any()) } returns fakeCategoriesResponse
            repository.getCategory("category").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSourceCategory.getCategory(any()) }
            }
        }
    }

    @Test
    fun `get categories, with result error`() {
        runTest {
            coEvery { remoteDataSourceCategory.getCategory(any()) } throws IllegalStateException("Mock error")
            repository.getCategory("category").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSourceCategory.getCategory(any()) }
            }
        }
    }

    @Test
    fun `get products, with result loading`() {
        val mockProductResponse = mockk<FoodResponse>()
        runTest {
            coEvery { remoteDataSourceFood.getFoods() } returns mockProductResponse
            repository.getFoods().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSourceFood.getFoods() }
            }
        }
    }

    @Test
    fun `get products, with result success`() {
        val fakeProductItemResponse = FoodViewParam(
            id = 1,
            imageUrl = "url",
            nama = "url",
            hargaFormat = "url",
            harga = 1,
            detail = "url",
            alamatResto = "url"
        )
        val fakeProductsResponse = FoodResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeProductItemResponse)
        )
        runTest {
            coEvery { remoteDataSourceFood.getFoods() } returns fakeProductsResponse
            repository.getFoods().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { remoteDataSourceFood.getFoods() }
            }
        }
    }

    @Test
    fun `get products, with result empty`() {
        val fakeProductsResponse = FoodResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSourceFood.getFoods() } returns fakeProductsResponse
            repository.getFoods().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSourceFood.getFoods() }
            }
        }
    }

    @Test
    fun `get products, with result error`() {
        runTest {
            coEvery { remoteDataSourceFood.getFoods() } throws IllegalStateException("Mock error")
            repository.getFoods().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSourceFood.getFoods() }
            }
        }
    }
}
