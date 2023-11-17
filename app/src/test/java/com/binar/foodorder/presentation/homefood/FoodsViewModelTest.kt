package com.binar.foodorder.presentation.homefood

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.binar.foodorder.data.repository.FoodsRepository
import com.binar.foodorder.tools.MainCoroutineRule
import com.binar.foodorder.tools.getOrAwaitValue
import com.binar.foodorder.util.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class FoodsViewModelTest {
    @MockK
    private lateinit var repo: FoodsRepository
    private lateinit var viewModel: FoodsViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            FoodsViewModel(repo),
            recordPrivateCalls = true
        )
        coEvery { repo.getFoods() } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }
        coEvery { repo.getListCategory() } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }
        coEvery { repo.getCategory(any()) } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }
    }

    @Test
    fun `getDataFoods`() {
        viewModel.getFoods()
        val result = viewModel.responseFood.getOrAwaitValue()
        assertTrue(result is ResultWrapper.Success)
        assertEquals(result.payload?.size, 3)
    }

    @Test
    fun `getDataFoodsCategory`() {
        viewModel.getCategoryList()

        val result = viewModel.responseCategory.getOrAwaitValue()
        assertTrue(result is ResultWrapper.Success)
        assertEquals(result.payload?.size, 4)
    }

    @Test
    fun `getDataFoodsCategoryList`() {
        viewModel.getCategory("category")
        val result = viewModel.responseCategoryMie.getOrAwaitValue()
        assertTrue(result is ResultWrapper.Success)
        assertEquals(result.payload?.size, 5)
    }
}
