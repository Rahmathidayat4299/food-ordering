package com.binar.foodorder.data.repository

import app.cash.turbine.test
import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.datasource.CartDatabaseDataSource
import com.binar.foodorder.data.local.database.entity.CartEntity
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSource
import com.binar.foodorder.data.network.model.OrderResponse
import com.binar.foodorder.model.Cart
import com.binar.foodorder.model.Food
import com.binar.foodorder.util.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class CartRepositoryImplTest {
    @MockK
    lateinit var localDatabaseDataSource: CartDatabaseDataSource

    @MockK
    lateinit var cartDataSource: CartDataSource

    @MockK
    lateinit var remoteDataSourceFood: FoodNetworkDataSource

    @MockK
    lateinit var remoteDataUser: FirebaseAuthDataSource
    private lateinit var repository: CartRepository
    private val fakeCartList = listOf(
        CartEntity(
            id = 1,
            foodId = 1,
            foodName = "Sate Cirebon",
            foodPrice = 12000.0,
            foodImgUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        ),
        CartEntity(
            id = 2,
            foodId = 1,
            foodName = "Sate Padang",
            foodPrice = 14000.0,
            foodImgUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        )
    )
    val mockCart = Cart(
        id = 1,
        foodId = 1,
        foodName = "Sate",
        foodPrice = 12000.0,
        foodImgUrl = "url",
        itemQuantity = 2,
        itemNotes = "notes"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository =
            CartRepositoryImpl(localDatabaseDataSource, remoteDataSourceFood, remoteDataUser)
    }

    @Test
    fun deleteAll() {
        coEvery { localDatabaseDataSource.deleteAll() } returns Unit
        runTest {
            val result = repository.deleteAll()
            coVerify { localDatabaseDataSource.deleteAll() }
            assertEquals(result, Unit)
        }
    }

    @Test
    fun `get user card data, result success`() {
        every { localDatabaseDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.first?.size, 2)
                assertEquals(data.payload?.second, 52000.0)
                verify { localDatabaseDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data, result loading`() {
        every { localDatabaseDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2101)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { localDatabaseDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data, result error`() {
        every { localDatabaseDataSource.getAllCarts() } returns flow {
            throw IllegalStateException("Mock Error")
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                verify { localDatabaseDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data, result empty`() {
        every { localDatabaseDataSource.getAllCarts() } returns flow {
            emit(listOf())
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                verify { localDatabaseDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `create cart loading,food id not null`() {
        runTest {
            val mockProduct = mockk<Food>(relaxed = true)
            coEvery { localDatabaseDataSource.insertCart(any()) } returns 1
            repository.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { localDatabaseDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart success,food id not null`() {
        runTest {
            val mockProduct = mockk<Food>(relaxed = true)
            coEvery { localDatabaseDataSource.insertCart(any()) } returns 1
            repository.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    assertEquals(result.payload, true)
                    coVerify { localDatabaseDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart error, food id not null`() {
        runTest {
            val mockProduct = mockk<Food>(relaxed = true)
            coEvery { localDatabaseDataSource.insertCart(any()) } throws IllegalStateException("Mock Error")
            repository.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { localDatabaseDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart error, food id null`() {
        runTest {
            val mockFood = mockk<Food>(relaxed = true) {
                every { id } returns null
            }
            coEvery { localDatabaseDataSource.insertCart(any()) } returns 1
            repository.createCart(mockFood, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    assertEquals(result.exception?.message, "Food ID not found")
                    coVerify(atLeast = 0) { localDatabaseDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `decrease cart when quantity less than or equal 0`() {
        val mockCart = Cart(
            id = 1,
            foodId = 1,
            foodName = "Sate",
            foodPrice = 12000.0,
            foodImgUrl = "url",
            itemQuantity = 0,
            itemNotes = "notes"
        )
        coEvery { localDatabaseDataSource.deleteCart(any()) } returns 1
        coEvery { localDatabaseDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDatabaseDataSource.deleteCart(any()) }
                coVerify(atLeast = 0) { localDatabaseDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `decrease cart when quantity more than  0`() {
        val mockCart = Cart(
            id = 1,
            foodId = 1,
            foodName = "Sate",
            foodPrice = 12000.0,
            foodImgUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        )
        coEvery { localDatabaseDataSource.deleteCart(any()) } returns 1
        coEvery { localDatabaseDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDatabaseDataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { localDatabaseDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `increase cart`() {
        coEvery { localDatabaseDataSource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDatabaseDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `set cart notes`() {
        coEvery { localDatabaseDataSource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDatabaseDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `delete cart`() {
        coEvery { localDatabaseDataSource.deleteCart(any()) } returns 1
        runTest {
            repository.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDatabaseDataSource.deleteCart(any()) }
            }
        }
    }

    @Test
    fun `create order loading`() {
        runTest {
            val mockCart1 = mockk<Cart>(relaxed = true)
            val listCart = listOf(mockCart1)
            coEvery { remoteDataSourceFood.createOrder(any()) } returns OrderResponse(
                code = 200,
                message = "Success",
                status = true
            )
            repository.createOrder(listCart).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                println(data)
                println(data.exception?.message.orEmpty())
            }
        }
    }
}
