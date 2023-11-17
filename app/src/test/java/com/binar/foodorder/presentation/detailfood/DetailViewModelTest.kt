package com.binar.foodorder.presentation.detailfood

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.binar.foodorder.data.repository.CartRepository
import com.binar.foodorder.tools.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

class DetailViewModelTest {
    @MockK
    private lateinit var extras: Bundle

    @MockK
    private lateinit var cartRepository: CartRepository

    lateinit var viewModel: DetailViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
//        every { extras.getParcelable<Food>(DetailFoodActivity.EXTRA_FOOD) } returns mockk()

        viewModel = spyk(
            DetailViewModel(
                extras,
                cartRepository

            ),
            recordPrivateCalls = true
        )
    }
}
