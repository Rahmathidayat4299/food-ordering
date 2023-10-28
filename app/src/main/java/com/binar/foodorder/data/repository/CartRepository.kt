package com.binar.foodorder.data.repository

import android.util.Log
import com.binar.foodorder.data.local.database.datasource.CartDataSource
import com.binar.foodorder.data.local.database.entity.CartEntity
import com.binar.foodorder.data.local.database.mapper.toCartEntity
import com.binar.foodorder.data.local.database.mapper.toCartList
import com.binar.foodorder.data.network.api.FoodNetworkDataSource
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSource
import com.binar.foodorder.data.network.model.OrderItem
import com.binar.foodorder.data.network.model.OrderRequest
import com.binar.foodorder.model.Cart
import com.binar.foodorder.model.Food
import com.binar.foodorder.model.toUser
import com.binar.foodorder.util.ResultWrapper
import com.binar.foodorder.util.proceed
import com.binar.foodorder.util.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepository {
    fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>>
    suspend fun createCart(food: Food, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun createOrder(items: List<Cart>): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
}

class CartRepositoryImpl(
    private val dataSource: CartDataSource,
    private val foodNetworkDataSource: FoodNetworkDataSource,
    private val user: FirebaseAuthDataSource
) : CartRepository {

    override fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>> {
        return dataSource.getAllCarts().map {
            proceed {
                val cartList = it.toCartList()
                val totalPrice = cartList.sumOf {
                    val quantity = it.itemQuantity
                    val pricePerItem = it.foodPrice
                    quantity * pricePerItem
                }
                Pair(cartList, totalPrice)
            }
        }.map {
            if (it.payload?.first?.isEmpty() == true) {
                ResultWrapper.Empty(it.payload)
            } else {
                it
            }
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

    // gunakan di detail
    override suspend fun createCart(
        food: Food,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return food.id.let { foodId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(
                        foodId = foodId,
                        itemQuantity = totalQuantity,
                        foodName = food.nama,
                        foodPrice = food.harga.toDouble(),
                        foodImgUrl = food.imageUrl
                    )
                )
                affectedRow > 0
            }
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun createOrder(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = items.map {
                OrderItem(
                    catatan = it.itemNotes,
                    harga = it.foodPrice.toInt(),
                    nama = it.foodName.toString(),
                    qty = it.itemQuantity
                )
            }
            Log.d(
                "CartRepositoryImpl",
                "OrderItems: $orderItems"
            ) // Add this line to log orderItems

            val totalQuantity = dataSource.getAllCarts().first()
            val totalPrice = totalQuantity.sumOf { it.foodPrice * it.itemQuantity }
            val user = user.getCurrentUser().toUser()?.fullName
            val orderRequest = OrderRequest(
                orders = orderItems,
                total = totalPrice.toInt(),
                username = user.toString() // Fix total here
            )
            Log.d(
                "CartRepositoryImpl",
                "OrderRequest: $orderRequest"
            ) // Add this line to log orderRequest

            val createOrderResponse = foodNetworkDataSource.createOrder(orderRequest)
            Log.d(
                "CartRepositoryImpl",
                "CreateOrderResponse: $createOrderResponse"
            ) // Add this line to log the response

            val isOrderCreated = createOrderResponse.status == true
            isOrderCreated
        }
    }

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }
}
