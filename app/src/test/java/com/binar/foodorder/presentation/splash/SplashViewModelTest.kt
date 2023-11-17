package com.binar.foodorder.presentation.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.binar.foodorder.data.repository.UserRepository
import com.binar.foodorder.tools.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SplashViewModelTest {
    @MockK
    private lateinit var repository: UserRepository
    lateinit var viewModel: SplashViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            SplashViewModel(
                repository
            ),
            recordPrivateCalls = true
        )
    }

    @Test
    fun `test isUserLoggedIn when user is logged in`() {
        every { repository.isLoggedIn() } returns true
        val result = viewModel.isUserLoggedIn()
        assertTrue(result)
    }

    @Test
    fun `test isUserLoggedIn when user is not logged in`() {
        every { repository.isLoggedIn() } returns false
        val result = viewModel.isUserLoggedIn()
        assertFalse(result)
    }
}
